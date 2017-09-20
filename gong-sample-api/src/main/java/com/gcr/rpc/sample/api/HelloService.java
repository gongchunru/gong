package com.gcr.rpc.sample.api;

/**
 * Created by gongchunru
 * Date：2017/9/1.
 * Time：14:59
 */

public interface HelloService {

    String hello(String name);

    String hello(Person person);
}
