package com.gcr.rpc.server;

import org.springframework.stereotype.Component;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * RPC 服务注解（标注在服务实现类上）
 * Created by gongchunru
 * Date：2017/8/24.
 * Time：19:24
 */

@Target({ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
@Component
public @interface RpcService {

    /**
     * 服务接口类
     * @return
     */
    Class<?> value();


    /**
     * 服务版本号
     * @return
     */
    String version() default "";

}
