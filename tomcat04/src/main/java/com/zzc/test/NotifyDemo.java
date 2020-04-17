package com.zzc.test;

/**
 * @author zzc
 * @create 2020-04-17 15:22
 */
public class NotifyDemo {

    public static void main(String[] args) {
        Object o = new Object();
        // first thread
        Thread download = new Thread() {

            @Override
            public void run() {
                System.out.println("currnetThreadName=" + currentThread().getName());
                System.out.println("start downloading picture");
                for (int i = 0; i < 101; i+= 10) {
                    System.out.println("download " + i + "%");
                    try {
                        Thread.sleep(500);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
                System.out.println("download picture successfully");

                synchronized (o) {
                    // 唤醒
                    o.notify();
                }

                System.out.println("start downloading attachment");
                for (int i = 0; i < 102; i+= 10) {
                    System.out.println("download " + i + "%");
                    try {
                        Thread.sleep(500);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
                System.out.println("download attachment successfully");
            }
        };

        // second thread
        Thread show = new Thread() {

            @Override
            public void run() {
                System.out.println("currnetThreadName=" + currentThread().getName());
                synchronized (o) {
                    try {
                        o.wait();
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                    System.out.println("start show~~~");
                    System.out.println("finish show pictrue!!!");
                }
            }
        };

        // start tow threads
        download.start();
        show.start();
    }
}
