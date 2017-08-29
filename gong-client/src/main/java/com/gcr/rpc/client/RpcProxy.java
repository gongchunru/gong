package com.gcr.rpc.client;

import com.gcr.common.bean.RpcRequest;
import com.gcr.common.bean.RpcResponse;
import com.gcr.common.util.StringUtil;
import com.gcr.rpc.registry.ServiceDiscovery;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.SystemUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import sun.rmi.runtime.Log;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;
import java.util.UUID;

/**
 * Created by gongchunru
 * Date：2017/8/28.
 * Time：11:51
 * RPC代理 （用于创建RPC服务代理）
 */
public class RpcProxy {
    private static final Logger LOGGER = LoggerFactory.getLogger(RpcProxy.class);

    private String serviceAddress;

    private ServiceDiscovery serviceDiscovery;

    public RpcProxy(String serviceAddress) {
        this.serviceAddress = serviceAddress;
    }

    public RpcProxy(ServiceDiscovery serviceDiscovery) {
        this.serviceDiscovery = serviceDiscovery;
    }

    @SuppressWarnings("unchecked")
    public <T> T create(final Class<T> interfaceClass){
        return create(interfaceClass, "");
    }
    @SuppressWarnings("unchecked")
    public <T> T create(final Class<?> interfaceClass, final String serviceVersion){
        return (T) Proxy.newProxyInstance(interfaceClass.getClassLoader(),
                new Class<?>[]{interfaceClass},
                new InvocationHandler() {
                    @Override
                    public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
                        //创建 RPC 请求对象并设置请求属性
                        RpcRequest request = new RpcRequest();
                        request.setRequestId(UUID.randomUUID().toString());
                        request.setInterfaceName(method.getDeclaringClass().getName());
                        request.setMethodName(method.getName());
                        request.setParameterTypes(method.getParameterTypes());
                        request.setParameters(args);

                        //获取RPC服务地址
                        if (serviceDiscovery != null){
                            String serviceName = interfaceClass.getName();
                            if (StringUtils.isNotEmpty(serviceVersion)){
                                serviceName +="-" + serviceVersion;
                            }

                            serviceAddress = serviceDiscovery.discover(serviceName);
                            LOGGER.debug("discover service: {} => {}", serviceName, serviceAddress);
                        }
                        if (StringUtil.isEmpty(serviceAddress)){
                            throw new RuntimeException("server address is empty");
                        }
                        //从RPC 服务地址中解析主机名与端口号
                        String[] array = StringUtil.split(serviceAddress,":");
                        String host = array[0];
                        int port = Integer.parseInt(array[1]);

                        //创建RPC客户端对象并发送RPC请求
                        RpcClient client = new RpcClient(host,port);
                        long time = System.currentTimeMillis();
                        RpcResponse response = client.send(request);
                        LOGGER.debug("time: {}ms", System.currentTimeMillis() - time);

                        if (response == null) {
                            throw new RuntimeException("response is null");
                        }
                        // 返回 RPC 响应结果
                        if (response.hasException()) {
                            throw response.getException();
                        } else {
                            return response.getResult();
                        }
                    }
                }

        );
    }
}
