package com.zzc;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.io.InputStream;

/**
 * @author zzc
 * @create 2020-04-13 16:54
 */
public class Request {
    private InputStream in;
    private String uri;

    private static final Logger LOGGER = LoggerFactory.getLogger(HttpServer.class);

    public Request(InputStream in) {
        this.in = in;
    }

    /**
     * Read a set of characters from the socket
     */
    public void parse() {
        StringBuffer buffer = new StringBuffer(2048);
        byte[] bytes = new byte[2048];
        int len = 0;

        try {
            len = in.read(bytes);
        } catch (IOException e) {
            LOGGER.error("read from inputstream fail", e);
            len = -1;
        }
        for (int i = 0; i < len; i++) {
            buffer.append((char)bytes[i]);
        }
        LOGGER.debug(buffer.toString());
        uri = parseUri(buffer.toString());
    }


    /**
     * 从请求行中获取URI， 在请求中搜索第一个和第二个空格
     *
     * GET /hello.txt HTTP/1.1
     *
     * @param request
     * @return
     */
    private String parseUri(String request) {
        // 1.第一个空格下标
        int index = request.indexOf(' ');
        if (-1 != index) {
            // 2.第二个空格下标
            int index2 = request.indexOf(' ', index + 1);
            if (index2 > index) {
                return request.substring(index + 2, index2);
            }
        }
        return null;
    }

    public String getUri() {
        return uri;
    }
}
