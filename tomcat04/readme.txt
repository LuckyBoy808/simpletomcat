HttpConnector类中的start()方法作用：

1.threadStart()：开启一个线程（执行HttpConnector中的run()方法）来循环接收socket。接收到socket后，分配给HttpProcessor

2.
while (curProcessors < minProcessors) {
    if ((maxProcessors > 0) && (curProcessors >= maxProcessors))
        break;
    HttpProcessor processor = newProcessor();
    recycle(processor);
}
默认创建5个HttpProcessor，每个HttpProcessor都会启动一个线程，等待分配来的socket并进行处理
（会执行HttpProcessor类中的run()方法，但执行到Socket socket = await();语句时，当前线程会阻塞，
因为available默认值为false，在HttpProcessor类中的await()方法中会执行wait()方法来阻塞当前线程。
那么，HttpProcessor类中的run()方法将不会继续执行下去）


当执行HttpConnector中的run()方法中的processor.assign(socket);语句时，会调用HttpProcessor类中的
assign()方法，此方法作用是：唤醒一个阻塞的线程。那么HttpProcessor中的run()方法将会继续执行。程序将会
调用process(socket)方法，处理请求