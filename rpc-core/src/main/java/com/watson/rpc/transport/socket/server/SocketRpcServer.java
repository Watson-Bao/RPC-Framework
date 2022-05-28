package com.watson.rpc.transport.socket.server;

import com.watson.rpc.config.RpcServiceConfig;
import com.watson.rpc.factory.SingletonFactory;
import com.watson.rpc.provider.ServiceProvider;
import com.watson.rpc.provider.ServiceProviderImpl;
import com.watson.rpc.transport.RpcServer;
import com.watson.rpc.enume.RpcError;
import com.watson.rpc.exception.RpcException;
import com.watson.rpc.serializer.CommonSerializer;
import com.watson.rpc.utils.ThreadPoolFactory;
import lombok.extern.slf4j.Slf4j;

import java.io.IOException;
import java.net.*;
import java.util.concurrent.ExecutorService;

/**
 * 远程方法调用的提供者（服务端）
 *
 * @author watson
 */
@Slf4j
public class SocketRpcServer implements RpcServer {

    private final ExecutorService threadPool;;
    private final int port;
    private CommonSerializer serializer;
    private final ServiceProvider serviceProvider = SingletonFactory.getInstance(ServiceProviderImpl.class);

    public SocketRpcServer(String host, int port) {
        this.port = port;
        threadPool = ThreadPoolFactory.createDefaultThreadPool("socket-rpc-server");
    }

    @Override
    public void start() {
        if (serializer == null) {
            log.error("未设置序列化器");
            throw new RpcException(RpcError.SERIALIZER_NOT_FOUND);
        }
        try (ServerSocket serverSocket = new ServerSocket(port)) {
            log.info("服务器启动……");
            Socket socket;
            while ((socket = serverSocket.accept()) != null) {
                log.info("消费者连接: {}:{}", socket.getInetAddress(), socket.getPort());
                threadPool.execute(new RequestHandlerThread(socket, serializer));
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
        if(serializer == null) {
            log.error("未设置序列化器");
            throw new RpcException(RpcError.SERIALIZER_NOT_FOUND);
        }
        serviceProvider.publishService(rpcServiceConfig,port);
    }
}
