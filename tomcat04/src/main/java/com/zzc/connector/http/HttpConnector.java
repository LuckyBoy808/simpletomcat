package com.zzc.connector.http;

import com.zzc.Constants;
import com.zzc.Lifecycle;
import com.zzc.connector.Connector;
import com.zzc.container.Container;
import com.zzc.exception.LifecycleException;
import com.zzc.net.ServerSocketFactory;
import com.zzc.net.DefaultServerSocketFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.net.BindException;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Stack;
import java.util.Vector;

/**
 * @description 用于接收http请求
 * @author zzc
 * @create 2020-04-15 17:04
 */
public class HttpConnector implements Connector, Lifecycle, Runnable {
    // Log
    private static final Logger LOGGER = LoggerFactory.getLogger(HttpConnector.class);

    /**
     * Has this component been initialized yet?
     */
    private boolean initialized = false;

    /**
     * Has this component been started yet?
     */
    private boolean started = false;

    /**
     * The shutdown signal to our background thread
     */
    private boolean stopped = false;

    private ServerSocket serverSocket = null;

    private String address = null;

    private int port = 8080;

    private int acceptCount = 10;

    private ServerSocketFactory factory = null;

    private String threadName = null;

    /**
     * The background thread.
     */
    private Thread thread = null;

    /**
     * Timeout value on the incoming connection.
     * Note : a value of 0 means no timeout.
     */
    private int connectionTimeout = Constants.DEFAULT_CONNECTION_TIMEOUT;

    /**
     * Use TCP no delay ?
     */
    private boolean tcpNoDelay = true;

    /**
     * The thread synchronization object.
     */
    private Object threadSync = new Object();

    /**
     * The set of processors that have been created but are not currently
     * being used to process a request.
     */
    private Stack processors = new Stack();

    /**
     * The set of processors that have ever been created.
     */
    private Vector created = new Vector();

    /**
     * The minimum number of processors to start at initialization time.
     */
    protected int minProcessors = 5;

    /**
     * The maximum number of processors allowed, or <0 for unlimited.
     */
    private int maxProcessors = 20;

    /**
     * The current number of processors that have been created.
     */
    private int curProcessors = 0;

    /**
     * The Container used for processing requests received by this Connector.
     */
    protected Container container = null;

    /**
     * Create (or allocate) and return an available processor for use in
     * processing a specific HTTP request, if possible.  If the maximum
     * allowed processors have already been created and are in use, return
     * <code>null</code> instead.
     */
    private HttpProcessor createProcessor() {

        synchronized (processors) {
            // 如果processors池中有，则直接返回
            if (processors.size() > 0) {
                return ((HttpProcessor) processors.pop());
            }
            // 如果processors池中没有
            // 判断当前processors是否大于最大processors数；
            // 当前processors数大于最大processors数：返回null；如果小于：new一个processor
            if ((maxProcessors > 0) && (curProcessors < maxProcessors)) {
                return (newProcessor());
            } else {
                if (maxProcessors < 0) {
                    return (newProcessor());
                } else {
                    return (null);
                }
            }
        }

    }

    /**
     * Create and return a net processor suitable for processing HTTP
     * requests and returning the corresponding responses.
     */
    private HttpProcessor newProcessor() {

        HttpProcessor processor = new HttpProcessor(this, curProcessors++);
        if (processor instanceof Lifecycle) {
            try {
                ((Lifecycle) processor).start();
            } catch (LifecycleException e) {
                LOGGER.error("net Processor failure", e);
                return (null);
            }
        }
        created.addElement(processor);
        return (processor);

    }

    /**
     * Recycle the specified Processor so that it can be used again.
     *
     */
    void recycle(HttpProcessor processor) {
        processors.push(processor);
    }

    @Override
    public void run() {

        // Loop until we receive a shutdown command
        while (!stopped){
            // 第一步，Accept the next incoming connection from the server socket
            Socket socket = null;
            try {
                socket = serverSocket.accept();
                if (connectionTimeout > 0){
                    socket.setSoTimeout(connectionTimeout);
                }
                socket.setTcpNoDelay(tcpNoDelay);
            } catch (IOException e) {
                try {
                    // If reopening fails, exit
                    synchronized (threadSync) {
                        if (started && !stopped){
                            LOGGER.error("accept error: ", e);
                        }
                        if (!stopped) {
                            serverSocket.close();
                            serverSocket = open();
                        }
                    }
                } catch (IOException ioe){
                    LOGGER.error("socket reopen, io problem: ", ioe);
                    break;
                }

                continue;
            }

            // 第二步、Hand this socket off to an appropriate processor
            HttpProcessor processor = createProcessor();
            if (processor == null) {
                try {
                    LOGGER.error("httpConnector.noProcessor");
                    socket.close();
                } catch (IOException e) {
                    ;
                }
                continue;
            }
            processor.assign(socket);

            // The processor will recycle itself when it finishes
        }

        synchronized (threadSync) {
            threadSync.notifyAll();
        }

    }

    @Override
    public void initialize() {
        if (initialized){
            LOGGER.error("httpConnector has initialized");
            throw new RuntimeException ();
        }

        this.initialized=true;

        // Establish a server socket on the specified port
        try {
            serverSocket = open();
        }catch (IOException e){
            LOGGER.error("open server socket failure", e);
            throw new RuntimeException(e);
        }
    }

    private ServerSocket open() throws IOException {

        ServerSocketFactory factory = getFactory();

        // If no address is specified, open a connection on all addresses
        if (address == null) {
            LOGGER.error("httpConnector.allAddresses");
            try {
                return (factory.createSocket(port, acceptCount));
            } catch (BindException be){
                throw new BindException(be.getMessage() + ":" + port);
            }
        }

        // Open a server socket on the specified address
        try {
            InetAddress is = InetAddress.getByName(address);
            LOGGER.error("httpConnector.anAddress {}", address);
            try {
                return (factory.createSocket(port, acceptCount, is));
            } catch (BindException be) {
                throw new BindException(be.getMessage() + ":" + address +  ":" + port);
            }
        } catch (Exception e) {
            LOGGER.error("httpConnector.noAddress {}", address);
            try {
                return (factory.createSocket(port, acceptCount));
            } catch (BindException be) {
                throw new BindException(be.getMessage() + ":" + port);
            }
        }

    }

    public ServerSocketFactory getFactory() {

        if (this.factory == null) {
            synchronized (this) {
                this.factory = new DefaultServerSocketFactory();
            }
        }
        return (this.factory);

    }

    /**
     * Start the background processing thread.
     */
    private void threadStart() {

        LOGGER.info("httpConnector.starting");

        thread = new Thread(this, threadName);
        // 标记为守护线程
        thread.setDaemon(true);
        // 会调用HttpConnector类中的run()方法
        thread.start();

    }

    /**
     * Stop the background processing thread.
     */
    private void threadStop() {

        LOGGER.info("httpConnector.stopping");

        stopped = true;
        try {
            threadSync.wait(5000);
        } catch (InterruptedException e) {
            ;
        }
        thread = null;

    }

    @Override
    public void start() throws LifecycleException {
        if(started){
            LOGGER.error("httpConnector.alreadyStarted");
            throw new LifecycleException("httpConnector.alreadyStarted");
        }
        threadName = "HttpConnector[" + port + "]";
        started = true;

        // Start our background thread， 在一个独立线程中处理到达的连接
        threadStart();

        // Create the specified minimum number of processors， 默认先创建最小数量的processor，放入空闲栈中
        while (curProcessors < minProcessors) {
            if ((maxProcessors > 0) && (curProcessors >= maxProcessors))
                break;
            HttpProcessor processor = newProcessor();
            recycle(processor);
        }
    }

    @Override
    public void stop() throws LifecycleException {
        // Validate and update our current state
        if (!started)
            throw new LifecycleException("httpConnector.notStarted");

        started = false;

        // Gracefully shut down all processors we have created
        for (int i = created.size() - 1; i >= 0; i--) {
            HttpProcessor processor = (HttpProcessor) created.elementAt(i);
            if (processor instanceof Lifecycle) {
                try {
                    ((Lifecycle) processor).stop();
                } catch (LifecycleException e) {
                    LOGGER.error("HttpConnector.stop", e);
                }
            }
        }

        synchronized (threadSync) {
            // Close the server socket we were using
            if (serverSocket != null) {
                try {
                    serverSocket.close();
                } catch (IOException e) {
                    ;
                }
            }
            // Stop our background thread
            threadStop();
        }
        serverSocket = null;
    }

    /**
     * Return the Container used for processing requests received by this
     * Connector.
     */
    public Container getContainer() {
        return (container);
    }

    /**
     * Set the Container used for processing requests received by this
     * Connector.
     *
     * @param container The net Container to use
     */
    public void setContainer(Container container) {
        this.container = container;
    }

    /**
     * Return the port number on which we listen for HTTP requests.
     */
    public int getPort() {
        return (this.port);
    }
}
