package com.zzc.connector.http;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;

/**
 * @description 用于接收http请求
 * @author zzc
 * @create 2020-04-15 17:04
 */
public class HttpConnector implements Runnable {
    // Log
    private static final Logger LOGGER = LoggerFactory.getLogger(HttpConnector.class);

    boolean stopped;

    private String scheme = "http";

    @Override
    public void run() {
        ServerSocket serverSocket = null;
        int port = 8080;
        try {
            serverSocket = new ServerSocket(port, 1, InetAddress.getByName("127.0.0.1"));
        } catch (IOException e) {
            LOGGER.error("connect socket failure", e);
            System.exit(1);
        }

        while (!stopped) {
            // Accept the next incoming connection from the server socket
            Socket socket = null;
            try {
                socket = serverSocket.accept();
                LOGGER.debug("-------- debug: {} ------------", Thread.currentThread().getName());
            } catch (Exception e) {
                continue;
            }
            // Hand this socket off to an HttpProcessor
            HttpProcessor processor = new HttpProcessor(this);
            processor.process(socket);
        }
    }

    public void start() {
        Thread thread = new Thread(this);
        thread.start();
    }
}
