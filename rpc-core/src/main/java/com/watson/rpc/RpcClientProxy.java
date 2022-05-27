package com.watson.rpc;

import com.alibaba.fastjson2.JSON;
import com.watson.rpc.socket.client.SocketClient;
import com.watson.rpc.entity.RpcRequest;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;
import java.nio.charset.StandardCharsets;

/**
 * RPC客户端动态代理
 * @author watson
 */
@Slf4j
@AllArgsConstructor
public class RpcClientProxy implements InvocationHandler {

    private final RpcClient client;

    /**
     * 得到代理方法
     * @param clazz
     * @return
     * @param <T>
     */
    @SuppressWarnings("unchecked")
    public <T> T getProxy(Class<T> clazz) {
        return (T) Proxy.newProxyInstance(clazz.getClassLoader(), new Class<?>[]{clazz}, this);
    }



    @Override
    public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
        log.info("调用方法: {}#{}", method.getDeclaringClass().getName(), method.getName());
        RpcRequest rpcRequest = new RpcRequest(method.getDeclaringClass().getName(),
                method.getName(), args, method.getParameterTypes());

        return client.sendRequest(rpcRequest);
    }
}
