package com.zzc.net;

import java.io.IOException;
import java.net.InetAddress;
import java.net.ServerSocket;

/**
 * @author zzc
 * @create 2020-04-17 11:52
 */
public interface ServerSocketFactory {
    ServerSocket createSocket (int port) throws IOException;

    ServerSocket createSocket (int port, int backlog) throws IOException;

    ServerSocket createSocket (int port, int backlog, InetAddress ifAddress) throws IOException;
}
