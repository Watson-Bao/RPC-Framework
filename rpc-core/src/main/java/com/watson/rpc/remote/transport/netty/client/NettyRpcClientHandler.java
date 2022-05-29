package com.watson.rpc.remote.transport.netty.client;

import com.watson.rpc.factory.SingletonFactory;
import com.watson.rpc.remote.dto.RpcResponse;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.util.ReferenceCountUtil;
import lombok.extern.slf4j.Slf4j;

/**
 * 自定义客户端 ChannelHandler 来处理服务端发过来的数据
 *
 * @author watson
 */
@Slf4j
public class NettyRpcClientHandler extends SimpleChannelInboundHandler<RpcResponse<Object>> {
    private final UnprocessedRequests unprocessedRequests;

    public NettyRpcClientHandler() {
        this.unprocessedRequests = SingletonFactory.getInstance(UnprocessedRequests.class);
    }


    @Override
    protected void channelRead0(ChannelHandlerContext ctx, RpcResponse<Object> msg) throws Exception {
        try {
            log.info(String.format("客户端接收到消息: %s", msg));
            unprocessedRequests.complete(msg);
        } finally {
            ReferenceCountUtil.release(msg);
        }
    }

    /**
     * 处理客户端消息发生异常的时候被调用
     */
    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) {
        log.error("处理过程调用时有错误发生:");
        cause.printStackTrace();
        ctx.close();
    }
}
