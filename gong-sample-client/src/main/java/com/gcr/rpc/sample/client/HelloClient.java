package com.gcr.rpc.sample.client;

import com.gcr.rpc.client.RpcProxy;
import com.gcr.rpc.sample.api.HelloService;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

/**
 * Created by gongchunru
 * Date：2017/9/1.
 * Time：16:02
 */

public class HelloClient {

    public static void main(String[] args) {
        ApplicationContext context = new ClassPathXmlApplicationContext("spring.xml");

        RpcProxy rpcProxy = context.getBean(RpcProxy.class);

        HelloService helloService = rpcProxy.create(HelloService.class);
        String result = helloService.hello("这是客户端");
        System.out.println(result);


        HelloService helloService2 = rpcProxy.create(HelloService.class,"sample.hello2");

        String result2 = helloService2.hello("这是第二条");
        System.out.println(result2);

        System.exit(0);


    }
}
