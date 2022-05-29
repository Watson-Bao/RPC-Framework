package com.watson.rpc.remote.transport.netty.server;

import com.watson.rpc.factory.SingletonFactory;
import com.watson.rpc.handler.RpcRequestHandler;
import com.watson.rpc.remote.dto.RpcRequest;
import com.watson.rpc.remote.dto.RpcResponse;
import com.watson.rpc.utils.concurrent.threadpool.CustomThreadPoolConfig;
import com.watson.rpc.utils.concurrent.threadpool.ThreadPoolFactoryUtils;
import io.netty.channel.ChannelFutureListener;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.util.ReferenceCountUtil;
import lombok.extern.slf4j.Slf4j;

import java.util.concurrent.ExecutorService;

/**
 * Netty中处理RpcRequest的Handler  Customize the ChannelHandler of the server to process the data sent by the client.
 * <p>
 * 如果继承自 SimpleChannelInboundHandler 的话就不要考虑 ByteBuf 的释放 ，{@link SimpleChannelInboundHandler} 内部的
 * channelRead 方法会替你释放 ByteBuf ，避免可能导致的内存泄露问题。详见《Netty进阶之路 跟着案例学 Netty》
 *
 * @author watson
 */
@Slf4j
public class NettyRpcServerHandler extends SimpleChannelInboundHandler<RpcRequest> {

    private final RpcRequestHandler rpcRequestHandler;

    public NettyRpcServerHandler() {
        this.rpcRequestHandler = SingletonFactory.getInstance(RpcRequestHandler.class);
    }

    @Override
    protected void channelRead0(ChannelHandlerContext ctx, RpcRequest msg) throws Exception {
        try {
            log.info("服务器接收到请求: {}", msg);
            //执行目标方法（客户端需要执行的方法）并且返回方法结果
            RpcResponse<Object> response = rpcRequestHandler.handle(msg);
            log.info("服务器处理得到请求结果: {}", response.toString());
            if (ctx.channel().isActive() && ctx.channel().isWritable()) {
                //返回方法执行结果给客户端
                ctx.writeAndFlush(response).addListener(ChannelFutureListener.CLOSE_ON_FAILURE);
            } else {
                log.error("not writable now, message dropped");
            }
        } finally {
            //确保 ByteBuf 被释放，不然可能会有内存泄露问题
            ReferenceCountUtil.release(msg);
        }
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        log.error("处理过程调用时有错误发生:");
        cause.printStackTrace();
        ctx.close();
    }
}
