package com.watson.rpc.transport.netty.client;

import com.watson.rpc.registry.NacosServiceDiscovery;
import com.watson.rpc.registry.ServiceDiscovery;
import com.watson.rpc.transport.RpcClient;
import com.watson.rpc.entity.RpcRequest;
import com.watson.rpc.entity.RpcResponse;
import com.watson.rpc.enume.RpcError;
import com.watson.rpc.exception.RpcException;
import com.watson.rpc.serializer.CommonSerializer;
import io.netty.bootstrap.Bootstrap;
import io.netty.channel.Channel;
import io.netty.channel.ChannelOption;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.util.AttributeKey;
import lombok.extern.slf4j.Slf4j;

import java.net.InetSocketAddress;
import java.util.concurrent.atomic.AtomicReference;

/**
 * @author watson
 */
@Slf4j
public class NettyRpcClient implements RpcClient {
    private static final Bootstrap bootstrap;
    private final ServiceDiscovery serviceDiscovery;
    private CommonSerializer serializer;

    static {
        EventLoopGroup group = new NioEventLoopGroup();
        bootstrap = new Bootstrap();
        bootstrap.group(group)
                .channel(NioSocketChannel.class)
                .option(ChannelOption.SO_KEEPALIVE, true);
    }


    public NettyRpcClient() {
        this.serviceDiscovery = new NacosServiceDiscovery();
    }

    /**
     * 客户端发送请求
     *
     * @param rpcRequest
     * @return
     */
    @Override
    public Object sendRpcRequest(RpcRequest rpcRequest) {
        if (serializer == null) {
            log.error("未设置序列化器");
            throw new RpcException(RpcError.SERIALIZER_NOT_FOUND);
        }
        AtomicReference<Object> result = new AtomicReference<>(null);

        try {
            InetSocketAddress inetSocketAddress = serviceDiscovery.lookupService(rpcRequest);
            Channel channel = ChannelProvider.get(inetSocketAddress, serializer);

            if (channel.isActive()) {
                channel.writeAndFlush(rpcRequest).addListener(future1 -> {
                    if (future1.isSuccess()) {
                        log.info(String.format("客户端发送消息: %s", rpcRequest));
                    } else {
                        log.error("发送消息时有错误发生: ", future1.cause());
                    }
                });
                channel.closeFuture().sync();
                AttributeKey<RpcResponse<Object>> key = AttributeKey.valueOf("rpcResponse" + rpcRequest.getRequestId());
                RpcResponse<Object> rpcResponse = channel.attr(key).get();
                result.set(rpcResponse);
            } else {
                channel.close();
                System.exit(0);
            }

        } catch (InterruptedException e) {
            log.error("发送消息时有错误发生: ", e);
        }
        return result.get();
    }

    @Override
    public void setSerializer(CommonSerializer serializer) {
        this.serializer = serializer;
    }
}
