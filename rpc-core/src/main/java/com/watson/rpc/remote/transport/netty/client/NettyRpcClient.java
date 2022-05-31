package com.watson.rpc.remote.transport.netty.client;

import com.watson.rpc.enume.SerializerEnum;
import com.watson.rpc.extension.ExtensionLoader;
import com.watson.rpc.factory.SingletonFactory;
import com.watson.rpc.registry.ServiceDiscovery;
import com.watson.rpc.remote.constant.RpcConstants;
import com.watson.rpc.remote.dto.RpcMessage;
import com.watson.rpc.remote.dto.RpcRequest;
import com.watson.rpc.remote.dto.RpcResponse;
import com.watson.rpc.remote.transport.RpcClient;
import com.watson.rpc.remote.transport.netty.codec.RpcMessageDecoder;
import com.watson.rpc.remote.transport.netty.codec.RpcMessageEncoder;
import io.netty.bootstrap.Bootstrap;
import io.netty.channel.*;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.handler.timeout.IdleStateHandler;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;

import java.net.InetSocketAddress;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.TimeUnit;

/**
 * @author watson
 */
@Slf4j
public class NettyRpcClient implements RpcClient {
    private final Bootstrap bootstrap;
    private final EventLoopGroup eventLoopGroup;
    private final ServiceDiscovery serviceDiscovery;

    private final UnprocessedRequests unprocessedRequests;
    private final ChannelProvider channelProvider;
    private byte serializerCode;

    public NettyRpcClient() {
        this(SerializerEnum.HESSIAN2.getCode());
    }

    public NettyRpcClient(byte serializerCode) {
        this.eventLoopGroup = new NioEventLoopGroup();
        this.bootstrap = new Bootstrap();
        this.bootstrap.group(this.eventLoopGroup)
                .channel(NioSocketChannel.class)
                //连接的超时时间，超过这个时间还是建立不上的话则代表连接失败
                .option(ChannelOption.CONNECT_TIMEOUT_MILLIS, 5000)
                //是否开启 TCP 底层心跳机制
                .option(ChannelOption.SO_KEEPALIVE, true)
                //TCP默认开启了 Nagle 算法，该算法的作用是尽可能的发送大数据快，减少网络传输。TCP_NODELAY 参数的作用就是控制是否启用 Nagle 算法。
                .option(ChannelOption.TCP_NODELAY, true)
                .handler(new ChannelInitializer<SocketChannel>() {
                    @Override
                    protected void initChannel(SocketChannel ch) {
                        ChannelPipeline p = ch.pipeline();
                        // 客户端每隔10s检测读空闲的channel，检测到读空闲时，则发送一次心跳包
                        p.addLast(new IdleStateHandler(10, 0, 0, TimeUnit.SECONDS));
                        /*自定义序列化编解码器*/
                        // RpcResponse -> ByteBuf
                        p.addLast(new RpcMessageEncoder());
                        // ByteBuf -> RpcRequest
                        p.addLast(new RpcMessageDecoder());
                        p.addLast(new NettyRpcClientHandler());
                    }
                });
        this.serviceDiscovery = ExtensionLoader.getExtensionLoader(ServiceDiscovery.class).getExtension("nacos");
        this.unprocessedRequests = SingletonFactory.getInstance(UnprocessedRequests.class);
        this.channelProvider = SingletonFactory.getInstance(ChannelProvider.class);
        this.serializerCode = serializerCode;
    }

    /**
     * 客户端发送请求
     *
     * @param rpcRequest
     * @return
     */
    @Override
    public Object sendRpcRequest(RpcRequest rpcRequest) {

        // build return value
        CompletableFuture<RpcResponse<Object>> resultFuture = new CompletableFuture<>();

        // get server address
        InetSocketAddress inetSocketAddress = serviceDiscovery.lookupService(rpcRequest);

        // get  server address related channel
        Channel channel = getChannel(inetSocketAddress);
        if (channel != null && channel.isActive()) {
            // put unprocessed request
            unprocessedRequests.put(rpcRequest.getRequestId(), resultFuture);
            RpcMessage rpcMessage = new RpcMessage();
            rpcMessage.setData(rpcRequest);
            rpcMessage.setCodec(serializerCode);
            rpcMessage.setMessageType(RpcConstants.REQUEST_TYPE);
            channel.writeAndFlush(rpcMessage).addListener((ChannelFutureListener) future -> {
                if (future.isSuccess()) {
                    log.info("客户端发送消息: [{}]", rpcMessage);
                } else {
                    future.channel().close();
                    resultFuture.completeExceptionally(future.cause());
                    log.error("发送消息时有错误发生: ", future.cause());
                }
            });
        } else {
            throw new IllegalStateException();
        }


        return resultFuture;
    }

    @SneakyThrows
    public Channel doConnect(InetSocketAddress inetSocketAddress) {
        CompletableFuture<Channel> completableFuture = new CompletableFuture<>();
        bootstrap.connect(inetSocketAddress).addListener((ChannelFutureListener) future -> {
            if (future.isSuccess()) {
                log.info("客户端连接成功!");
                completableFuture.complete(future.channel());
            } else {
                throw new IllegalStateException();
            }
        });
        return completableFuture.get();
    }

    public Channel getChannel(InetSocketAddress inetSocketAddress) {
        Channel channel = channelProvider.get(inetSocketAddress);
        if (channel == null) {
            channel = doConnect(inetSocketAddress);
            channelProvider.set(inetSocketAddress, channel);
        }
        return channel;
    }

    public void close() {
        log.info("call close method");
        eventLoopGroup.shutdownGracefully();
    }

    @Override
    public void setSerializer(byte serializerCode) {
        this.serializerCode = serializerCode;
    }
}
