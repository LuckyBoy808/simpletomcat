package com.zzc;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.*;
import java.net.Socket;

/**
 * @author zzc
 * @create 2020-04-13 21:19
 */
public class HttpServer implements Runnable {
    // log
    private static final Logger LOGGER = LoggerFactory.getLogger(HttpServer.class);
    // 资源路径
    public static final String WEB_APP = System.getProperty("user.dir") + File.separator + "webapp";

    // 输入流对象,读取浏览器请求
    private InputStream in;
    // 输出流对象，响应内容给浏览器
    private OutputStream out;

    /**
     * @description:初始化socket对象,获取对应 输入，输出流
     * @param socket
     */
    public HttpServer(Socket socket) {
        try {
            in = socket.getInputStream();
            out = socket.getOutputStream();
        } catch (IOException e) {
            LOGGER.error("communication with client fail! ", e);
        }
    }

    /**
     * 多线程调用
     */
    @Override
    public void run() {
        String filePath = request();
        response(filePath);
    }

    /**
     *  @description: 读取资源文件，响应给浏览器。
     * @param filePath 资源文件路径
     */
    private void response(String filePath) {
        File file = new File(HttpServer.WEB_APP + "/" + filePath);
        if (file.exists()) {
            // 1、资源存在，读取资源
            try {
                BufferedReader reader = new BufferedReader(new FileReader(file));
                StringBuffer sb = new StringBuffer();
                String line = null;
                while ((line = reader.readLine()) != null) {
                    sb.append(line).append("\r\n");
                }

                StringBuffer result = new StringBuffer();
                result.append("HTTP /1.1 200 ok \r\n");
                result.append("Content-Type:text/html \r\n");
                result.append("Content-Length:" + file.length() + "\r\n");
                result.append("\r\n" + sb.toString());

                out.write(result.toString().getBytes());
                out.flush();
                out.close();
            } catch (Exception e) {
                LOGGER.error("File Exist! Write Out Data Fail", e);
            }
        } else {
            // 2、资源不存在，提示 file not found
            StringBuffer error = new StringBuffer();
            error.append("HTTP /1.1 400 file not found \r\n");
            error.append("Content-Type:text/html \r\n");
            error.append("Content-Length:20 \r\n").append("\r\n");
            error.append("<h1 >File Not Found..</h1>");
            try {
                out.write(error.toString().getBytes());
                out.flush();
                out.close();
            } catch (IOException e) {
                LOGGER.error("File Not Found! Write Out Data Fail", e);
            }
        }
    }

    /**
     * @description:解析资源文件路径
     * @example: GET /index.html HTTP/1.1
     * @return
     */
    private String request() {
        // InputStreamReader类是从字节流到字符流的桥接器
        // 为了获得最高效率，在BufferedReader中包装InputStreamReader
        BufferedReader reader = new BufferedReader(new InputStreamReader(in));
        try {
            // 读取请求头， 如：GET /index.html HTTP/1.1
            String line = reader.readLine();
            String[] splits = line.split(" ");
            if (splits.length != 3) {
                // TODO
                return null;
            }
            LOGGER.debug(line);
            return splits[1];
        } catch (IOException e) {
            LOGGER.error("Get Request Header Fail! ", e);
        }
        // TODO
        return null;
    }
}
