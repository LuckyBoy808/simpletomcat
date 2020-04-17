package com.zzc.processor;

import com.zzc.connector.http.HttpRequest;
import com.zzc.connector.http.HttpResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @description 资源处理接口
 * @author zzc
 * @create 2020-04-15 20:40
 */
public abstract class Processor {
    protected Logger LOGGER = LoggerFactory.getLogger(getClass());

    private Processor processor;

    public void setProcessor(Processor processor) {
        this.processor = processor;
    }

    /**
     * 根据url判断由哪个具体的Processor去处理
     *
     * @param url
     */
    abstract boolean match(String url);

    /**
     * 责任链模式以合适的processor处理
     *
     * @param request
     * @param response
     */
    public void process(HttpRequest request, HttpResponse response){
        if (match(request.getRequestURI())){
            action(request, response);
        } else {
            processor.process(request, response);
        }
    }

    /**
     * 模板方法模式处理
     *
     * @param request
     * @param response
     */
    protected abstract void action(HttpRequest request, HttpResponse response);
}
