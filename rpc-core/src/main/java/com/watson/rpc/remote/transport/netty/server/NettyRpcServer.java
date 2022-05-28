package com.watson.rpc.remote.transport.netty.server;

import com.watson.rpc.codec.CommonDecoder;
import com.watson.rpc.codec.CommonEncoder;
import com.watson.rpc.config.RpcServiceConfig;
import com.watson.rpc.enume.RpcError;
import com.watson.rpc.exception.RpcException;
import com.watson.rpc.factory.SingletonFactory;
import com.watson.rpc.provider.ServiceProvider;
import com.watson.rpc.provider.ServiceProviderImpl;
import com.watson.rpc.remote.transport.RpcServer;
import com.watson.rpc.serializer.CommonSerializer;
import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.*;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.handler.logging.LogLevel;
import io.netty.handler.logging.LoggingHandler;
import lombok.extern.slf4j.Slf4j;

import java.net.InetAddress;
import java.net.UnknownHostException;

/**
 * NIO方式服务提供侧
 *
 * @author watson
 */
@Slf4j
public class NettyRpcServer implements RpcServer {
    private final int port;
    private final ServiceProvider serviceProvider = SingletonFactory.getInstance(ServiceProviderImpl.class);
    private CommonSerializer serializer;

    public NettyRpcServer(int port) {
        this.port = port;
    }

    /**
     * 服务器端开始监听请求
     */
    @Override
    public void start() {
        if (serializer == null) {
            log.error("未设置序列化器");
            throw new RpcException(RpcError.SERIALIZER_NOT_FOUND);
        }
        EventLoopGroup bossGroup = new NioEventLoopGroup();
        EventLoopGroup workerGroup = new NioEventLoopGroup();
        try {

            ServerBootstrap serverBootstrap = new ServerBootstrap();
            serverBootstrap.group(bossGroup, workerGroup)
                    .channel(NioServerSocketChannel.class)
                    .handler(new LoggingHandler(LogLevel.INFO))
                    //表示系统用于临时存放已完成三次握手的请求的队列的最大长度,如果连接建立频繁，服务器处理创建新连接较慢，可以适当调大这个参数
                    .option(ChannelOption.SO_BACKLOG, 256)
                    // 是否开启 TCP 底层心跳机制
                    .option(ChannelOption.SO_KEEPALIVE, true)
                    // TCP默认开启了 Nagle 算法，该算法的作用是尽可能的发送大数据快，减少网络传输。TCP_NODELAY 参数的作用就是控制是否启用 Nagle 算法
                    .childOption(ChannelOption.TCP_NODELAY, true)
                    // 当客户端第一次进行请求的时候才会进行初始化
                    .childHandler(new ChannelInitializer<SocketChannel>() {
                        @Override
                        protected void initChannel(SocketChannel ch) throws Exception {
                            ChannelPipeline pipeline = ch.pipeline();
                            pipeline.addLast(new CommonEncoder(serializer));
                            pipeline.addLast(new CommonDecoder());
                            pipeline.addLast(new NettyRpcServerHandler());
                        }
                    });
            String host = InetAddress.getLocalHost().getHostAddress();
            ChannelFuture future = serverBootstrap.bind(host, port).sync();
            future.channel().closeFuture().sync();

        } catch (InterruptedException e) {
            log.error("启动服务器时有错误发生: ", e);
        } catch (UnknownHostException e) {
            log.error("occur exception when getHostAddress", e);
        } finally {
            bossGroup.shutdownGracefully();
            workerGroup.shutdownGracefully();
        }

    }

    @Override
    public void setSerializer(CommonSerializer serializer) {
        this.serializer = serializer;
    }

    /**
     * 服务器端发布服务
     *
     * @param rpcServiceConfig
     */
    @Override
    public void registerService(RpcServiceConfig rpcServiceConfig) {
        if (serializer == null) {
            log.error("未设置序列化器");
            throw new RpcException(RpcError.SERIALIZER_NOT_FOUND);
        }
        serviceProvider.publishService(rpcServiceConfig, port);
    }
}
