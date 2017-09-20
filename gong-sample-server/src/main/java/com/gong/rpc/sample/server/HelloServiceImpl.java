package com.gong.rpc.sample.server;

import com.gcr.rpc.sample.api.HelloService;
import com.gcr.rpc.sample.api.Person;
import com.gcr.rpc.server.RpcService;

/**
 * Created by gongchunru
 * Date：2017/9/1.
 * Time：14:57
 */
@RpcService(HelloService.class)//指定远程接口
public class HelloServiceImpl implements HelloService {

    @Override
    public String hello(String name) {
        return "Hello "+ name;
    }

    @Override
    public String hello(Person person) {
        return "Hello " + person.getFirstName() + " " + person.getLastName();
    }
}
