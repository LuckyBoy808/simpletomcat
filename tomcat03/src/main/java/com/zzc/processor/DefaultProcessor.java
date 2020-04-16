package com.zzc.processor;

import com.zzc.connector.http.HttpRequest;
import com.zzc.connector.http.HttpResponse;

import java.io.IOException;
import java.io.PrintWriter;
import java.io.Writer;

/**
 * @author zzc
 * @create 2020-04-15 20:50
 */
public class DefaultProcessor extends Processor {

    @Override
    boolean match(String url) {
        return true;
    }

    @Override
    protected void action(HttpRequest request, HttpResponse response) {
        //Writer writer = null;
        PrintWriter writer = null;
        String msg = "<h1>no suitable processor</h1>";
        try {
            writer = response.getWriter();
            // 状态行
            writer.print("HTTP/1.1 200 OK\r\n");
            // 响应头
            writer.print("Content-Type:text/html\r\n");
            writer.print("Content-Length:" + msg.length() +"\r\n");
            writer.print("\r\n");
            // 响应正文
            writer.write(msg);
        } catch (IOException e) {
            LOGGER.error("get response writer failure");
        } finally {
            if (writer != null){
                writer.close();
            }
        }
    }
}
