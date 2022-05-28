package com.watson.rpc.proxy;

import com.watson.rpc.config.RpcServiceConfig;
import com.watson.rpc.remote.to.RpcRequest;
import com.watson.rpc.remote.to.RpcResponse;
import com.watson.rpc.remote.transport.RpcClient;
import com.watson.rpc.remote.utils.RpcMessageChecker;
import lombok.extern.slf4j.Slf4j;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;
import java.util.UUID;

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

        RpcResponse<Object> rpcResponse = (RpcResponse<Object>) rpcClient.sendRpcRequest(rpcRequest);
        RpcMessageChecker.check(rpcRequest, rpcResponse);
        return rpcResponse.getData();
    }
}
