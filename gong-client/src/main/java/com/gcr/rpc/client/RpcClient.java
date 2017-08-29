package com.gcr.rpc.client;

import com.gcr.common.bean.RpcRequest;
import com.gcr.common.bean.RpcResponse;
import com.gcr.common.codec.RpcDecoder;
import com.gcr.common.codec.RpcEncoder;
import io.netty.bootstrap.Bootstrap;
import io.netty.channel.*;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * RPC 客户端 用于发送RPC请求
 * Created by gongchunru
 * Date：2017/8/25.
 * Time：17:08
 */
public class RpcClient extends SimpleChannelInboundHandler<RpcResponse>{

    public static final Logger logger = LoggerFactory.getLogger(RpcClient.class);

    private final String host;

    private final int port;

    private RpcResponse response;

    public RpcClient(String host, int port) {
        this.host = host;
        this.port = port;
    }



    @Override
    protected void channelRead0(ChannelHandlerContext channelHandlerContext, RpcResponse rpcResponse) throws Exception {
        this.response = rpcResponse;
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        logger.debug("");
        ctx.close();
    }


    public RpcResponse send(RpcResponse rpcResponse) throws InterruptedException {

        EventLoopGroup group = new NioEventLoopGroup();
        try{
            //初始化netty客户端，并创建bootstrap对象
            Bootstrap bootstrap = new Bootstrap();
            bootstrap.group(group);
            bootstrap.channel(NioSocketChannel.class);
            bootstrap.handler(new ChannelInitializer<SocketChannel>() {
                @Override
                protected void initChannel(SocketChannel channel) throws Exception {
                    ChannelPipeline pipeline = channel.pipeline();
                    pipeline.addLast(new RpcEncoder(RpcRequest.class));// 编码 请求
                    pipeline.addLast(new RpcDecoder(RpcResponse.class)); // 解码 响应
                    pipeline.addLast(RpcClient.this);
                }
            });

            bootstrap.option(ChannelOption.TCP_NODELAY,true);
            //连接RPC服务器
            ChannelFuture future = bootstrap.connect(host, port).sync();

            //写入RPC请求数据并关闭连接
            Channel channel = future.channel();
            channel.writeAndFlush(response).sync();
            channel.closeFuture().sync();
            //返回RPC响应对象
            return response;
        }finally {
            group.shutdownGracefully();
        }
    }
}
