package com.watson.rpc.remote.transport.netty.client;

import com.watson.rpc.remote.dto.RpcResponse;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.util.AttributeKey;
import io.netty.util.ReferenceCountUtil;
import lombok.extern.slf4j.Slf4j;

/**
 * @author watson
 */
@Slf4j
public class NettyRpcClientHandler extends SimpleChannelInboundHandler<RpcResponse<Object>> {


    @Override
    protected void channelRead0(ChannelHandlerContext ctx, RpcResponse<Object> msg) throws Exception {
        try {
            log.info(String.format("客户端接收到消息: %s", msg));
            AttributeKey<RpcResponse<Object>> key = AttributeKey.valueOf("rpcResponse" + msg.getRequestId());
            ctx.channel().attr(key).set(msg);
            ctx.channel().close();
        } finally {
            ReferenceCountUtil.release(msg);
        }
    }
}
