package com.watson.rpc.remote.transport.socket.server;

import com.watson.rpc.config.CustomShutdownHook;
import com.watson.rpc.config.RpcServiceConfig;
import com.watson.rpc.enume.RpcError;
import com.watson.rpc.exception.RpcException;
import com.watson.rpc.factory.SingletonFactory;
import com.watson.rpc.provider.ServiceProvider;
import com.watson.rpc.provider.ServiceProviderImpl;
import com.watson.rpc.remote.transport.RpcServer;
import com.watson.rpc.serializer.Serializer;
import com.watson.rpc.serializer.Hessian2Serializer;
import com.watson.rpc.utils.concurrent.threadpool.ThreadPoolFactoryUtils;
import lombok.extern.slf4j.Slf4j;

import java.io.IOException;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.concurrent.ExecutorService;

/**
 * 远程方法调用的提供者（服务端）
 *
 * @author watson
 */
@Slf4j
public class SocketRpcServer implements RpcServer {

    private final ExecutorService threadPool;
    ;
    private final int port;
    private final ServiceProvider serviceProvider = SingletonFactory.getInstance(ServiceProviderImpl.class);
    private Serializer serializer;

    public SocketRpcServer(int port) {
        this(port, new Hessian2Serializer());
    }

    public SocketRpcServer(int port, Serializer serializer) {
        this.port = port;
        this.serializer = serializer;
        threadPool = ThreadPoolFactoryUtils.createCustomThreadPoolIfAbsent("socket-server-rpc-pool");
    }

    @Override
    public void start() {
        CustomShutdownHook.getCustomShutdownHook().clearAll();
        if (serializer == null) {
            log.error("未设置序列化器");
            throw new RpcException(RpcError.SERIALIZER_NOT_FOUND);
        }
        try (ServerSocket serverSocket = new ServerSocket()) {
            String host = InetAddress.getLocalHost().getHostAddress();
            serverSocket.bind(new InetSocketAddress(host, port));
            log.info("服务器启动……");
            Socket socket;
            while ((socket = serverSocket.accept()) != null) {
                log.info("消费者连接: {}:{}", socket.getInetAddress(), socket.getPort());
                threadPool.execute(new SocketRequestHandlerThread(socket, serializer));
            }
            threadPool.shutdown();
        } catch (IOException e) {
            log.error("服务器启动时有错误发生:", e);
        }
    }

    /**
     * 设置序列化器
     *
     * @param serializer
     */
    @Override
    public void setSerializer(Serializer serializer) {
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
