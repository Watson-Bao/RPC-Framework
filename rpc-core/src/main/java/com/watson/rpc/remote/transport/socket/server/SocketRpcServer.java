package com.watson.rpc.remote.transport.socket.server;

import com.watson.rpc.config.CustomShutdownHook;
import com.watson.rpc.config.RpcServiceConfig;
import com.watson.rpc.enume.SerializerEnum;
import com.watson.rpc.factory.SingletonFactory;
import com.watson.rpc.provider.ServiceProvider;
import com.watson.rpc.provider.ServiceProviderImpl;
import com.watson.rpc.remote.transport.RpcServer;
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
    private byte serializerCode;

    public SocketRpcServer(int port) {
        this(port, SerializerEnum.HESSIAN2.getCode());
    }

    public SocketRpcServer(int port, byte serializerCode) {
        this.port = port;
        this.serializerCode = serializerCode;
        threadPool = ThreadPoolFactoryUtils.createCustomThreadPoolIfAbsent("socket-server-rpc-pool");
    }

    @Override
    public void start() {
        CustomShutdownHook.getCustomShutdownHook().clearAll();

        try (ServerSocket serverSocket = new ServerSocket()) {
            String host = InetAddress.getLocalHost().getHostAddress();
            serverSocket.bind(new InetSocketAddress(host, port));
            log.info("服务器启动……");
            Socket socket;
            while ((socket = serverSocket.accept()) != null) {
                log.info("消费者连接: {}:{}", socket.getInetAddress(), socket.getPort());
                threadPool.execute(new SocketRequestHandlerThread(socket, serializerCode));
            }
            threadPool.shutdown();
        } catch (IOException e) {
            log.error("服务器启动时有错误发生:", e);
        }
    }


    @Override
    public void setSerializer(byte serializerCode) {
        this.serializerCode = serializerCode;
    }

    /**
     * 服务器端发布服务
     *
     * @param rpcServiceConfig
     */
    @Override
    public void registerService(RpcServiceConfig rpcServiceConfig) {
        serviceProvider.publishService(rpcServiceConfig, port);
    }
}
