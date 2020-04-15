package com.zzc;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.servlet.*;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.util.Enumeration;
import java.util.Locale;
import java.util.Map;

/**
 * @author zzc
 * @create 2020-04-13 16:54
 */
public class Request implements ServletRequest {
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
     * GET /hello.html HTTP/1.1
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
                // 此次的uri有带了"/"。如：/index.html
                return request.substring(index + 1, index2);
            }
        }
        return null;
    }

    public String getUri() {
        return uri;
    }

    @Override
    public Object getAttribute(String s) {
        return null;
    }

    @Override
    public Enumeration<String> getAttributeNames() {
        return null;
    }

    @Override
    public String getCharacterEncoding() {
        return null;
    }

    @Override
    public void setCharacterEncoding(String s) throws UnsupportedEncodingException {

    }

    @Override
    public int getContentLength() {
        return 0;
    }

    @Override
    public long getContentLengthLong() {
        return 0;
    }

    @Override
    public String getContentType() {
        return null;
    }

    @Override
    public ServletInputStream getInputStream() throws IOException {
        return null;
    }

    @Override
    public String getParameter(String s) {
        return null;
    }

    @Override
    public Enumeration<String> getParameterNames() {
        return null;
    }

    @Override
    public String[] getParameterValues(String s) {
        return new String[0];
    }

    @Override
    public Map<String, String[]> getParameterMap() {
        return null;
    }

    @Override
    public String getProtocol() {
        return null;
    }

    @Override
    public String getScheme() {
        return null;
    }

    @Override
    public String getServerName() {
        return null;
    }

    @Override
    public int getServerPort() {
        return 0;
    }

    @Override
    public BufferedReader getReader() throws IOException {
        return null;
    }

    @Override
    public String getRemoteAddr() {
        return null;
    }

    @Override
    public String getRemoteHost() {
        return null;
    }

    @Override
    public void setAttribute(String s, Object o) {

    }

    @Override
    public void removeAttribute(String s) {

    }

    @Override
    public Locale getLocale() {
        return null;
    }

    @Override
    public Enumeration<Locale> getLocales() {
        return null;
    }

    @Override
    public boolean isSecure() {
        return false;
    }

    @Override
    public RequestDispatcher getRequestDispatcher(String s) {
        return null;
    }

    @Override
    public String getRealPath(String s) {
        return null;
    }

    @Override
    public int getRemotePort() {
        return 0;
    }

    @Override
    public String getLocalName() {
        return null;
    }

    @Override
    public String getLocalAddr() {
        return null;
    }

    @Override
    public int getLocalPort() {
        return 0;
    }

    @Override
    public ServletContext getServletContext() {
        return null;
    }

    @Override
    public AsyncContext startAsync() throws IllegalStateException {
        return null;
    }

    @Override
    public AsyncContext startAsync(ServletRequest servletRequest, ServletResponse servletResponse) throws IllegalStateException {
        return null;
    }

    @Override
    public boolean isAsyncStarted() {
        return false;
    }

    @Override
    public boolean isAsyncSupported() {
        return false;
    }

    @Override
    public AsyncContext getAsyncContext() {
        return null;
    }

    @Override
    public DispatcherType getDispatcherType() {
        return null;
    }
}
