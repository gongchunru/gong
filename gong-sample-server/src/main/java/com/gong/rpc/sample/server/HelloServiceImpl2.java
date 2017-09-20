package com.gong.rpc.sample.server;

import com.gcr.rpc.sample.api.HelloService;
import com.gcr.rpc.sample.api.Person;
import com.gcr.rpc.server.RpcService;

/**
 * Created by gongchunru
 * Date：2017/9/1.
 * Time：15:31
 */
@RpcService(value = HelloService.class, version = "sample.hello2")
public class HelloServiceImpl2 implements HelloService {

    @Override
    public String hello(String name) {
        return "你好！"+name;
    }

    @Override
    public String hello(Person person) {
        return "你好 " + person.getFirstName() + " " + person.getLastName();
    }
}
