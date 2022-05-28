package com.watson.rpc.transport.netty.server;

import com.watson.rpc.factory.SingletonFactory;
import com.watson.rpc.handler.RpcRequestHandler;
import com.watson.rpc.entity.RpcRequest;
import com.watson.rpc.entity.RpcResponse;
import com.watson.rpc.utils.ThreadPoolFactory;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelFutureListener;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.util.ReferenceCountUtil;
import lombok.extern.slf4j.Slf4j;

import java.util.concurrent.ExecutorService;

/**
 * Netty中处理RpcRequest的Handler  Customize the ChannelHandler of the server to process the data sent by the client.
 *
 * 如果继承自 SimpleChannelInboundHandler 的话就不要考虑 ByteBuf 的释放 ，{@link SimpleChannelInboundHandler} 内部的
 * channelRead 方法会替你释放 ByteBuf ，避免可能导致的内存泄露问题。详见《Netty进阶之路 跟着案例学 Netty》
 * @author watson
 */
@Slf4j
public class NettyRpcServerHandler extends SimpleChannelInboundHandler<RpcRequest> {
    private  RpcRequestHandler rpcRequestHandler;
    private static final String THREAD_NAME_PREFIX = "netty-server-handler";
    private static final ExecutorService threadPool;
    static {
        threadPool = ThreadPoolFactory.createDefaultThreadPool(THREAD_NAME_PREFIX);
    }

    public NettyRpcServerHandler() {
        this.rpcRequestHandler = SingletonFactory.getInstance(RpcRequestHandler.class);
    }

    @Override
    protected void channelRead0(ChannelHandlerContext ctx, RpcRequest msg) throws Exception {
        threadPool.execute(() -> {
            try {
                log.info("服务器接收到请求: {}", msg);
                RpcResponse<Object> response = rpcRequestHandler.handle(msg);
                ChannelFuture future = ctx.writeAndFlush(response);
                future.addListener(ChannelFutureListener.CLOSE);
            } finally {
                ReferenceCountUtil.release(msg);
            }
        });
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        log.error("处理过程调用时有错误发生:");
        cause.printStackTrace();
        ctx.close();
    }
}
