package com.watson.rpc.proxy;

import com.watson.rpc.config.RpcServiceConfig;
import com.watson.rpc.remote.dto.RpcRequest;
import com.watson.rpc.remote.dto.RpcResponse;
import com.watson.rpc.remote.transport.RpcClient;
import com.watson.rpc.remote.transport.netty.client.NettyRpcClient;
import com.watson.rpc.remote.transport.socket.client.SocketRpcClient;
import com.watson.rpc.remote.utils.RpcMessageChecker;
import lombok.extern.slf4j.Slf4j;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;
import java.util.UUID;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;

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

        RpcResponse<Object> rpcResponse = null;
        if (rpcClient instanceof NettyRpcClient) {
            CompletableFuture<RpcResponse<Object>> completableFuture = (CompletableFuture<RpcResponse<Object>>) rpcClient.sendRpcRequest(rpcRequest);
            try {
                rpcResponse = completableFuture.get();
            } catch (InterruptedException | ExecutionException e) {
                log.error("请求结果获取异常", e);
                throw new RuntimeException(e);
            }
        }
        if (rpcClient instanceof SocketRpcClient) {
            rpcResponse = (RpcResponse<Object>) rpcClient.sendRpcRequest(rpcRequest);
        }
        RpcMessageChecker.check(rpcRequest, rpcResponse);
        return rpcResponse.getData();
    }
}
