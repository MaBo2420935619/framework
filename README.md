#该框架的实现内容
该框架主要实现了Spring框架的几个主要部分，并且进行了拓展

IOC，即依赖注入，以bean工厂的方式，对所有被@Bean标注的的类在BeanFactory进行注册，使用时只需要在BeanFactory进行获取。

AOP，即面向切面编程，通过不侵入式的编程，只需要对相关的类和方法加注解，就可实现对特定的类进行增强。

定时器，该框架是以多线程的方式，来实现定时任务。

JavaWeb，使用原生的socket通信来进行通信，优点是可以支持各种类型请求。该框架定义了一个@RequestMaping注解，可以配合该框架来进行类似于Spring框架的后端框架的开发。


版权声明：本文为CSDN博主「BR20200222」的原创文章，遵循CC 4.0 BY-SA版权协议，转载请附上原文出处链接及本声明。

原文链接：https://blog.csdn.net/weixin_47053123/article/details/121098976

