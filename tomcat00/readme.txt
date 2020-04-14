Java写一个简单的Web服务器Socket实现

实现思路：
1.使用 ServerSocket 监听某一端口，然后等待连接并获取 Socket对象。
2.创建一个类 HttpServer 继承 java.lang.Thread 类，重写 run()方法，执行浏览器请求。
3.获得浏览器请求，解析资源文件路径。
4.读取资源文件，响应给浏览器。