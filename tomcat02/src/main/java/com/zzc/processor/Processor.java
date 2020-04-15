package com.zzc.processor;

import com.zzc.Request;
import com.zzc.Response;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


/**
 * @description 资源处理接口
 * @author zzc
 * @create 2020-04-15 8:40
 */
public abstract class Processor {
    // Log
    protected static final Logger LOGGER = LoggerFactory.getLogger(Processor.class);

    private Processor processor;

    /**
     * @description 根据url判断由哪个具体的Processor去处理
     * @param url
     * @return
     */
    abstract boolean match(String url);

    /**
     * @description 责任链模式 来选择合适的processor进行处理
     * @param request
     * @param response
     */
    public void process(Request request, Response response) {
        if (match(request.getUri())) {
            action(request, response);
        } else {
            processor.process(request, response);
        }
    }

    /**
     * @description 模板方法模式处理
     * @param request
     * @param response
     */
    protected abstract void action(Request request, Response response);

    public void setProcessor(Processor processor) {
        this.processor = processor;
    }
}
