package com.zzc.processor;

import com.zzc.connector.http.HttpRequest;
import com.zzc.connector.http.HttpResponse;

import java.io.IOException;

/**
 * @author zzc
 * @create 2020-04-15 20:44
 */
public class StaticResourceProcessor extends Processor {
    @Override
    boolean match(String url) {
        return url == null ? false : url.startsWith("/resource");
    }

    @Override
    protected void action(HttpRequest request, HttpResponse response) {
        try {
            response.sendStaticResource();
        } catch (IOException e) {
            LOGGER.error("send static resource failure", e);
        }
    }
}
