package com.zzc;


import com.zzc.processor.DefaultProcessor;
import com.zzc.processor.Processor;
import com.zzc.processor.ServletProcessor;
import com.zzc.processor.StaticResourceProcessor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;

/**
 * @author zzc
 * @create 2020-04-13 12:30
 */
public class HttpServer {
    // log
    private static final Logger LOGGER = LoggerFactory.getLogger(HttpServer.class);
    // the shutdown command receiver
    private static boolean SHOWDOWN = false;
    //shutdown command
    private static final String SHUTDOWN_COMMAND = "/SHUTDOWN";


    public static void main(String[] args) {
        HttpServer.await();
    }

    private static void await() {
        ServerSocket serverSocket = null;
        int port = 8080;
        try {
            serverSocket = new ServerSocket(port, 1, InetAddress.getByName("127.0.0.1"));
        } catch (IOException e) {
            LOGGER.error("server socket created fail", e);
            System.exit(1);
        }

        // Loop waiting for a request
        while (!SHOWDOWN) {
            Socket socket = null;
            InputStream in = null;
            OutputStream out = null;

            try {
                socket = serverSocket.accept();
                in = socket.getInputStream();
                out = socket.getOutputStream();

                Request request = new Request(in);
                request.parse();

                Response response = new Response(out);
                response.setRequest(request);
                // check if this is a request for a servlet or a static resource
                // a request for a servlet begins with "/servlet/"
                Processor servletProcessor = new ServletProcessor();
                Processor staticSourceProcessor = new StaticResourceProcessor();
                Processor defaultProcessor = new DefaultProcessor();

                staticSourceProcessor.setProcessor(defaultProcessor);
                servletProcessor.setProcessor(staticSourceProcessor);
                servletProcessor.process(request, response);

                socket.close();

                //check if the previous uri is a shutdown command
                SHOWDOWN = request.getUri() == null ? false : request.getUri().equals(SHUTDOWN_COMMAND);

            } catch (IOException e) {
                LOGGER.error("communication with client fail! ", e);
                System.exit(1);
            }
        }
    }
}
