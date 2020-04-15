package com.zzc.processor;

import com.zzc.Request;
import com.zzc.Response;

/**
 * @description 静态资源处理器
 * @author zzc
 * @create 2020-04-15 9:02
 */
public class StaticResourceProcessor extends Processor {

    @Override
    boolean match(String url) {
        return url == null ? false : url.startsWith("/resource");
    }

    @Override
    protected void action(Request request, Response response) {
        response.sendStaticResource();
    }
}
