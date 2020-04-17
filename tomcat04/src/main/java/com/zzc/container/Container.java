package com.zzc.container;

import com.zzc.connector.http.HttpRequest;
import com.zzc.connector.http.HttpResponse;

import javax.servlet.ServletException;
import java.io.IOException;

/**
 * @author zzc
 * @create 2020-04-17 11:45
 */
public interface Container {
    void invoke(HttpRequest request, HttpResponse response) throws IOException, ServletException;
}
