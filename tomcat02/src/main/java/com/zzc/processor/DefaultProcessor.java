package com.zzc.processor;

import com.zzc.Request;
import com.zzc.Response;

/**
 * @description 默认都不匹配时，由该Processor处理
 * @author zzc
 * @create 2020-04-15 11:09
 */
public class DefaultProcessor extends Processor {
    @Override
    boolean match(String url) {
        return true;
    }

    @Override
    protected void action(Request request, Response response) {
        response.sendDefault();
    }
}
