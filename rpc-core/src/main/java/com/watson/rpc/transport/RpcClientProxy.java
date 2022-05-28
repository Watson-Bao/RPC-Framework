package com.watson.rpc.transport;

import com.watson.rpc.config.RpcServiceConfig;
import com.watson.rpc.entity.RpcRequest;
import com.watson.rpc.entity.RpcResponse;
import com.watson.rpc.transport.netty.client.NettyRpcClient;
import com.watson.rpc.transport.socket.client.SocketRpcClient;
import com.watson.rpc.utils.RpcMessageChecker;
import lombok.extern.slf4j.Slf4j;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;
import java.util.UUID;
import java.util.concurrent.CompletableFuture;

/**
 * RPC客户端动态代理
 *
 * @author watson
 */
@Slf4j
public class RpcClientProxy implements InvocationHandler {

    private final RpcClient rpcClient;
    private final RpcServiceConfig rpcServiceConfig;

    public RpcClientProxy(RpcClient rpcClient, RpcServiceConfig rpcServiceConfig) {
        this.rpcClient = rpcClient;
        this.rpcServiceConfig = rpcServiceConfig;
    }


    public RpcClientProxy(RpcClient rpcClient) {
        this.rpcClient = rpcClient;
        this.rpcServiceConfig = new RpcServiceConfig();
    }

    /**
     * 得到代理方法
     *
     * @param clazz
     * @param <T>
     * @return
     */
    @SuppressWarnings("unchecked")
    public <T> T getProxy(Class<T> clazz) {
        return (T) Proxy.newProxyInstance(clazz.getClassLoader(), new Class<?>[]{clazz}, this);
    }


    @Override
    public Object invoke(Object proxy, Method method, Object[] args) {
        log.info("调用方法: {}#{}", method.getDeclaringClass().getName(), method.getName());
        RpcRequest rpcRequest = new RpcRequest(UUID.randomUUID().toString(), method.getDeclaringClass().getName(),
                method.getName(), args, method.getParameterTypes(), rpcServiceConfig.getVersion(), rpcServiceConfig.getGroup());

        RpcResponse<Object> rpcResponse= (RpcResponse<Object>) rpcClient.sendRpcRequest(rpcRequest);
        RpcMessageChecker.check(rpcRequest, rpcResponse);
        return rpcResponse.getData();
    }
}
