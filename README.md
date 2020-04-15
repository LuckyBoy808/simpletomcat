# SimpleTomcat

## 一、前言
        深入学习tomcat源码，领悟其中的设计思想。这样既可提高自己对代码的阅读能力，也能使自己写出更加优雅的代码。

## 二、模块介绍
#### 2.1tomcat00
        使用java语言写一个简单的Web服务器Socket实现。
        创建一个ServerSocket，监听8080端口，然后request()方法解析监听到的socket，response()方法简单返回一个静态文本
        （index.html）。意在了解tomcat底层是 依据socket通信；
##### 代码测试
1. 启动WebServer类中的main()方法---程序会监听8080端口
2. 打开浏览器，在地址栏中输入：localhost:8080/index.html

#### 2.2tomcat01
        tomcat01将监听请求和请求处理抽象成了两个对象Request和Response，进行分开处理。这样就提升了扩展性了