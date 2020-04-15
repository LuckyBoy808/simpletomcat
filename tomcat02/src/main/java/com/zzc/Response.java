package com.zzc;

import com.zzc.constant.Constant;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


import javax.servlet.ServletOutputStream;
import javax.servlet.ServletResponse;
import java.io.*;
import java.util.Locale;


/**
 * @author zzc
 * @create 2020-04-13 18:53
 */
public class Response implements ServletResponse {
    private Request request;
    private OutputStream out;
    private PrintWriter writer;

    private static final Logger LOGGER = LoggerFactory.getLogger(HttpServer.class);

    public Response(OutputStream out) {
        this.out = out;
    }

    public void sendStaticResource() {
        try {
            File file = new File(Constant.WEB_APP, request.getUri());
            if (file.exists()) {
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
                StringBuffer error = new StringBuffer();
                error.append("HTTP/1.1 404 File Not Found\r\n");
                error.append("Content-Type: text/html\r\n");
                error.append("Content-Length:20\r\n").append("\r\n");
                error.append("<h1 >File Not Found</h1>");

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

    public void sendDefault() {
        try {
            StringBuffer msg = new StringBuffer();
            msg.append("HTTP/1.1 200 OK\r\n");
            msg.append("Content-Type: text/html\r\n");
            msg.append("Content-Length:30\r\n").append("\r\n");
            msg.append("<h1 >no suitable processor</h1>");

            out.write(msg.toString().getBytes());
        } catch (IOException e) {
            LOGGER.error("write out data fail", e);
        }
    }
    public void setRequest(Request request) {
        this.request = request;
    }

    @Override
    public String getCharacterEncoding() {
        return null;
    }

    @Override
    public String getContentType() {
        return null;
    }

    @Override
    public ServletOutputStream getOutputStream() throws IOException {
        return null;
    }

    @Override
    public PrintWriter getWriter() throws IOException {
        writer = new PrintWriter(out, true);
        return writer;
    }

    @Override
    public void setCharacterEncoding(String s) {

    }

    @Override
    public void setContentLength(int i) {

    }

    @Override
    public void setContentLengthLong(long l) {

    }

    @Override
    public void setContentType(String s) {

    }

    @Override
    public void setBufferSize(int i) {

    }

    @Override
    public int getBufferSize() {
        return 0;
    }

    @Override
    public void flushBuffer() throws IOException {

    }

    @Override
    public void resetBuffer() {

    }

    @Override
    public boolean isCommitted() {
        return false;
    }

    @Override
    public void reset() {

    }

    @Override
    public void setLocale(Locale locale) {

    }

    @Override
    public Locale getLocale() {
        return null;
    }
}
