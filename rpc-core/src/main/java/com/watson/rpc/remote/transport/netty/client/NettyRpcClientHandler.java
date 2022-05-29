package com.watson.rpc.remote.transport.netty.client;

import com.watson.rpc.factory.SingletonFactory;
import com.watson.rpc.remote.constant.RpcConstants;
import com.watson.rpc.remote.dto.RpcMessage;
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
public class NettyRpcClientHandler extends SimpleChannelInboundHandler<Object> {
    private final UnprocessedRequests unprocessedRequests;

    public NettyRpcClientHandler() {
        this.unprocessedRequests = SingletonFactory.getInstance(UnprocessedRequests.class);
    }


    /**
     * Read the message transmitted by the server
     */
    @Override
    protected void channelRead0(ChannelHandlerContext ctx, Object msg) {
        try {
            log.info(String.format("客户端接收到消息: %s", msg));
            if (msg instanceof RpcMessage) {
                RpcMessage tmp = (RpcMessage) msg;
                byte messageType = tmp.getMessageType();
                if (messageType == RpcConstants.HEARTBEAT_RESPONSE_TYPE) {
                    log.info("heart [{}]", tmp.getData());
                } else if (messageType == RpcConstants.RESPONSE_TYPE) {
                    RpcResponse<Object> rpcResponse = (RpcResponse<Object>) tmp.getData();
                    unprocessedRequests.complete(rpcResponse);
                }
            }
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
