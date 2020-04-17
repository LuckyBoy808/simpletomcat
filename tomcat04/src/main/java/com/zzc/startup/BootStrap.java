package com.zzc.startup;

import com.zzc.connector.http.HttpConnector;
import com.zzc.container.SimpleContainer;

/**
 * @author zzc
 * @create 2020-04-17 10:47
 */
public final class BootStrap {

    public static void main(String[] args) {
        HttpConnector connector = new HttpConnector();
        SimpleContainer container = new SimpleContainer();
        connector.setContainer(container);

        try {
            // initialize主要是open服务端socket，默认监听8080端口
            connector.initialize();
            //start主要是将服务端监听的socket交给processor处理，其中processor添加了一个缓存池，避免了每次都创建一个processor
            connector.start();

            //make the application wait util we press any key
            System.in.read();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
