package com.zzc;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.*;

/**
 * @author zzc
 * @create 2020-04-13 18:53
 */
public class Response {
    private Request request;
    private OutputStream out;

    //private static final int BUFFER_SIZE = 1024;
    private static final Logger LOGGER = LoggerFactory.getLogger(HttpServer.class);

    public Response(OutputStream out) {
        this.out = out;
    }

    public void sendStaticResource() {
        //byte[] bytes = new byte[BUFFER_SIZE];
        //FileInputStream in = null;

        try {
            File file = new File(HttpServer.WEB_APP, request.getUri());
            if (file.exists()) {
                /*in = new FileInputStream(file);
                int len = in.read(bytes, 0, BUFFER_SIZE);
                while (len != -1) {
                    out.write(bytes, 0, len);
                    len = in.read(bytes, 0, BUFFER_SIZE);
                }*/
                BufferedReader reader = new BufferedReader(new FileReader(file));
                StringBuffer sb = new StringBuffer();
                String line = null;
                while (null != (line = reader.readLine())) {
                    sb.append(line).append("\r\n");
                }

                StringBuffer result = new StringBuffer();
                result.append("HTTP/1.1 200 OK\r\n");
                result.append("Content-Type:text/html\r\n");
                result.append("Content-Length:" + file.length() + "\r\n");
                result.append("\r\n" + sb.toString());

                out.write(result.toString().getBytes());
                out.flush();
                out.close();
            } else {
                // 字符串拼接耗性能
                /*String errorMsg = "HTTP/1.1 404 File Not Found\r\n" +
                        "Content-Type: text/html\r\n" +
                        "Content-Length: 23\r\n" +
                        "\r\n" +
                        "<h1>File Not Found</h1>";
                out.write(errorMsg.getBytes());*/
                StringBuffer error = new StringBuffer();
                error.append("HTTP/1.1 404 File Not Found\r\n");
                error.append("Content-Type: text/html\r\n");
                error.append("Content-Length:20\r\n").append("\r\n");
                error.append("<h1 >File Not Found..</h1>");

                out.write(error.toString().getBytes());
            }
        } catch (FileNotFoundException e) {
            LOGGER.error("File Not Found", e);
        } catch (IOException e) {
            LOGGER.error("write out data fail", e);
        } finally {
            try {
                if (null != out) {
                    out.close();
                }
            } catch (IOException e) {
                LOGGER.error("Close FileInputStream Fail", e);
            }
        }
    }
    public void setRequest(Request request) {
        this.request = request;
    }
}
