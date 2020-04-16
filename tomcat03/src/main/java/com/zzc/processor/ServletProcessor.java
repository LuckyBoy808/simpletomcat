package com.zzc.processor;

import com.zzc.connector.http.HttpRequest;
import com.zzc.connector.http.HttpRequestFacade;
import com.zzc.connector.http.HttpResponse;
import com.zzc.connector.http.HttpResponseFacade;
import com.zzc.constant.Constant;

import javax.servlet.Servlet;
import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.net.URLClassLoader;
import java.net.URLStreamHandler;

/**
 * @author zzc
 * @create 2020-04-15 20:45
 */
public class ServletProcessor extends Processor {
    @Override
    boolean match(String url) {
        return url == null ? false : url.startsWith("/servlet");
    }

    @Override
    protected void action(HttpRequest request, HttpResponse response) {
        String uri = request.getRequestURI();
        String servletName = uri.substring(uri.lastIndexOf("/") + 1);
        URLClassLoader loader = null;
        try {
            // create a URLClassLoader
            URL[] urls = new URL[1];
            File classPath = new File(Constant.WEB_APP);
            URLStreamHandler streamHandler = null;
            String repository = (new URL("file", null, classPath.getCanonicalPath() + File.separator)).toString();
            urls[0] = new URL(null, repository, streamHandler);
            loader = new URLClassLoader(urls);
        } catch (IOException e) {
            LOGGER.error("", e);
        }
        Class myClass = null;
        try {
            myClass = loader.loadClass(servletName);
        } catch (ClassNotFoundException e) {
            LOGGER.error("load class failure", e);
        }

        Servlet servlet = null;

        try {
            servlet = (Servlet) myClass.newInstance();
            HttpResponseFacade responseFacade = new HttpResponseFacade(response);
            HttpRequestFacade requestFacade = new HttpRequestFacade(request);
            servlet.service(requestFacade, responseFacade);
            ((HttpResponse) response).finishResponse();
        } catch (Exception e) {
            LOGGER.error("", e);
        } catch (Throwable e) {
            LOGGER.error("", e);
        }
    }
}
