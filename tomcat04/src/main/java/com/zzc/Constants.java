package com.zzc;

import java.io.File;

/**
 * @author zzc
 * @create 2020-04-15 20:12
 */
public final class Constants {
    public static final String WEB_APP = String.format("%s%swebapp", System.getProperty("user.dir"), File.separator);
    public static final String PACKAGE = "com.zzc.connector.http";
    public static final int DEFAULT_CONNECTION_TIMEOUT = 60000;
    public static final int PROCESSOR_IDLE = 0;
    public static final int PROCESSOR_ACTIVE = 1;
}
