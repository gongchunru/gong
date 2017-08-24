package com.gcr.rpc.server;

import com.gcr.common.bean.RpcRequest;
import com.gcr.common.bean.RpcResponse;
import com.gcr.common.util.StringUtil;
import io.netty.channel.ChannelFutureListener;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import net.sf.cglib.reflect.FastClass;
import net.sf.cglib.reflect.FastMethod;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.reflect.InvocationTargetException;
import java.util.Map;

/**
 * Created by gongchunru
 * Date：2017/8/24.
 * Time：19:21
 */
public class RpcServerHandler extends SimpleChannelInboundHandler<RpcRequest> {

    private static final Logger logger = LoggerFactory.getLogger(RpcServerHandler.class);

    private final Map<String, Object> handlerMap;


    public RpcServerHandler(Map<String, Object> handlerMap) {
        this.handlerMap = handlerMap;

    }

    @Override
    protected void channelRead0(ChannelHandlerContext channelHandlerContext, RpcRequest request) throws Exception {
        //创建并初始化 RPC 响应对象
        RpcResponse response = new RpcResponse();
        response.setRequestId(request.getRequestId());
        try {
            Object result = handle(request);
            response.setResult(result);
        } catch (Exception e) {
            logger.error("handler result failure",e);
            response.setException(e);
        }

        //写入RPC响应对象并自动关闭连接
        channelHandlerContext.writeAndFlush(response).addListener(ChannelFutureListener.CLOSE);
    }

    private Object handle(RpcRequest request) throws InvocationTargetException {
        //获取服务对象
        String serviceName = request.getInterfaceName();
        String serviceVersion = request.getServiceVersion();
        if (StringUtil.isNotEmpty(serviceVersion)){
            serviceName += "-" + serviceVersion;
        }

        Object serviceBean = handlerMap.get(serviceName);
        if (serviceBean == null){
            throw new RuntimeException(String.format("can not find service bean by key: %s", serviceName));
        }
        //获取反射调用所需参数
        Class<?> serviceClass = serviceBean.getClass();
        String methodName = request.getMethodName();
        Class<?>[] parameterTypes = request.getParameterType();
        Object[] parameters = request.getParameters();

        //执行反射调用

        //使用CGLib执行反射调用
        FastClass serviceFastClass = FastClass.create(serviceClass);
        FastMethod serviceFastMethod = serviceFastClass.getMethod(methodName, parameterTypes);
        return serviceFastMethod.invoke(serviceBean,parameters);

    }


    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        logger.error("server caught exception",cause);
        ctx.close();
    }
}
