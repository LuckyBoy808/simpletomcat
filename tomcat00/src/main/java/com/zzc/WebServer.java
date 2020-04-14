package com.zzc;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

/**
 * @author zzc
 * @create 2020-04-13 21:15
 * @description: 使用socket 自己写一个web服务器 ---监听端口，获取socket 对象。
 */
public class WebServer {
    // log
    private static final Logger LOGGER = LoggerFactory.getLogger(HttpServer.class);

    public void start(int port) {
        try {
            ServerSocket serverSocket = new ServerSocket(port);
            while (true) {
                Socket socket = serverSocket.accept();
                HttpServer httpServer = new HttpServer(socket);
                new Thread(httpServer).start();
            }
        } catch (IOException e) {
            LOGGER.error("server socket created fail", e);
            System.exit(1);
        }
    }

    public static void main(String[] args) {
        new WebServer().start(8080);
    }
}
