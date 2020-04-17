package com.zzc.container;

import com.zzc.connector.http.HttpRequest;
import com.zzc.connector.http.HttpResponse;
import com.zzc.processor.DefaultProcessor;
import com.zzc.processor.Processor;
import com.zzc.processor.ServletProcessor;
import com.zzc.processor.StaticResourceProcessor;

import javax.servlet.ServletException;
import java.io.File;
import java.io.IOException;

/**
 * @author zzc
 * @create 2020-04-17 11:46
 */
public class SimpleContainer implements Container {

    public static final String WEB_APP = System.getProperty("user.dir") + File.separator + "webapp";

    public SimpleContainer(){}

    public void invoke(HttpRequest request, HttpResponse response) throws IOException, ServletException {
        //check if this is a request for a servlet or a static resource
        //a request for a servlet begins with "/servlet/"
        Processor servletProcessor = new ServletProcessor();
        Processor staticProcessor = new StaticResourceProcessor();
        Processor defaultProcessor = new DefaultProcessor();
        staticProcessor.setProcessor(defaultProcessor);
        servletProcessor.setProcessor(staticProcessor);
        servletProcessor.process(request, response);
    }

}
