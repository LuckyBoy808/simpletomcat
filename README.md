# SimpleTomcat

## 一、前言
        深入学习tomcat源码，领悟其中的设计思想。这样既可提高自己对代码的阅读能力，也能使自己写出更加优雅的代码。
        根据某位大佬的gitHub学习的，其github地址为：https://github.com/w1992wishes/tomcat-work

## 二、模块介绍
### 2.1tomcat00
        使用java语言写一个简单的Web服务器Socket实现。
        创建一个ServerSocket，监听8080端口，然后request()方法解析监听到的socket，response()方法简单返回一个静态文本
        （index.html）。意在了解tomcat底层是 依据socket通信；
#### 代码测试
1. 启动WebServer类中的main()方法---程序会监听8080端口
2. 打开浏览器，在地址栏中输入：localhost:8080/index.html

### 2.2tomcat01
        tomcat01将监听请求和请求处理抽象成了两个对象Request和Response，进行分开处理。这样就提升了扩展性了

### 2.3tomcat02
        tomcat01只能加载静态资源（index.html），tomcat02区分静态资源和Servlet的加载，先从监听到的连接中获取url，
        从url中获取servlet 的名字（无包名），然后是利用反射生成servlet实例，再调用servlet中的service()方法
##### 代码测试
1. 启动HttpServer类中的main()方法---程序会监听8080端口
2. 访问静态资源（webapp/resource/index.html）：在浏览器的地址栏中输入：localhost:8080/resource/index.html
3. 访问Servlet（PrimitiveServlet）：在浏览器的地址栏中输入：localhost:8080/servlet/PrimitiveServlet

### 2.3tomcat03
    tomcat03在tomcat02的基础上进行了拆分和补充
1. 首先增加了一个专门的启动类Bootstrap
2. 其次将HttpServer一分为二，一是HttpConnector，起一个线程专门用来接收http请求；二是HttpProcessor，用于创建HttpRequest、HttpResponse，
并做一些解析任务，最后同样交由Processor来处理。
3. 其他新增的类都是辅助解析request和response。

##### 代码测试
1. 启动Bootstrap类中的main()方法---程序会监听8080端口
2. 访问静态资源（webapp/resource/index.html）：在浏览器的地址栏中输入：localhost:8080/resource/index.html
3. 访问Servlet（TestServlet）：在浏览器的地址栏中输入：localhost:8080/servlet/TestServlet