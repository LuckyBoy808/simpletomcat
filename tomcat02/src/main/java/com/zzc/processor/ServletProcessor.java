package com.zzc.processor;

import com.zzc.Request;
import com.zzc.Response;
import com.zzc.constant.Constant;
import com.zzc.facade.RequestFacade;
import com.zzc.facade.ResponseFacade;

import javax.servlet.Servlet;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.net.URLClassLoader;
import java.net.URLStreamHandler;

/**
 * @description servlet处理器
 * @author zzc
 * @create 2020-04-15 10:30
 */
public class ServletProcessor extends Processor {
    @Override
    boolean match(String url) {
        return url == null ? false : url.startsWith("/servlet");
    }

    @Override
    protected void action(Request request, Response response) {
        String uri = request.getUri();
        // /servlet/PrimitiveServlet  ==> PrimitiveServlet
        String servletName = uri.substring(uri.lastIndexOf('/') + 1);
        URLClassLoader loader = null;
        try {
            URL[] urls = new URL[1];
            URLStreamHandler streamHandler = null;
            File classPath = new File(Constant.WEB_APP);
            // the classPath of repository is taken from the createClassLoader method in org.apache.catalina.startup.ClassLoaderFactory
            String repository = (new URL("file", null, classPath.getCanonicalPath() + File.separator)).toString();

            LOGGER.debug("class path: {}", classPath.getCanonicalPath());
            LOGGER.debug("url: {}", repository);

            // the code for forming the URL is taken form the addRepository method in org.apache.catalina.loader.StandardClassLoader
            urls[0] = new URL(null, repository, streamHandler);
            // URLClassLoader：URLClassLoader Load the .class file dynamically
            loader = new URLClassLoader(urls);
        } catch (IOException e) {
            LOGGER.error("servlet process fail", e);
        }

        Class clazz = null;
        try {
            clazz = loader.loadClass(servletName);
        } catch (ClassNotFoundException e) {
            LOGGER.error("load class fail", e);
        }

        Servlet servlet = null;
        try {
            servlet = (Servlet) clazz.newInstance();
            RequestFacade requestFacade = new RequestFacade(request);
            ResponseFacade responseFacade = new ResponseFacade(response);
            servlet.service((ServletRequest) requestFacade, (ServletResponse) responseFacade);
        } catch (InstantiationException e) {
            LOGGER.error("", e);
        } catch (IllegalAccessException e) {
            LOGGER.error("", e);
        } catch (ServletException e) {
            LOGGER.error("", e);
        } catch (IOException e) {
            LOGGER.error("", e);
        }
    }
}
