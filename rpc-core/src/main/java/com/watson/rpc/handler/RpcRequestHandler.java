package com.watson.rpc.handler;

import com.watson.rpc.enume.ResponseCode;
import com.watson.rpc.factory.SingletonFactory;
import com.watson.rpc.provider.ServiceProvider;
import com.watson.rpc.provider.ServiceProviderImpl;
import com.watson.rpc.remote.to.RpcRequest;
import com.watson.rpc.remote.to.RpcResponse;
import lombok.extern.slf4j.Slf4j;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

/**
 * 进行过程调用的处理器
 *
 * @author watson
 */
@Slf4j
public class RpcRequestHandler {
    private final ServiceProvider serviceProvider;

    public RpcRequestHandler() {
        serviceProvider = SingletonFactory.getInstance(ServiceProviderImpl.class);
    }

    public RpcResponse<Object> handle(RpcRequest rpcRequest) {
        RpcResponse<Object> response = null;
        Object service = serviceProvider.getService(rpcRequest.getRpcServiceName());
        try {
            response = invokeTargetMethod(rpcRequest, service);
            log.info("服务:{} 成功调用方法:{}", rpcRequest.getInterfaceName(), rpcRequest.getMethodName());
        } catch (IllegalAccessException | InvocationTargetException e) {
            log.error("调用或发送时有错误发生：", e);
        }
        return response;
    }

    /**
     * 通过传输过来的request中方法的相关信息反射出目标方法并执行
     *
     * @param rpcRequest
     * @param service
     * @return
     * @throws IllegalAccessException
     * @throws InvocationTargetException
     */
    private RpcResponse<Object> invokeTargetMethod(RpcRequest rpcRequest, Object service) throws IllegalAccessException, InvocationTargetException {
        Method method;
        try {
            method = service.getClass().getMethod(rpcRequest.getMethodName(), rpcRequest.getParamTypes());
            return RpcResponse.success(method.invoke(service, rpcRequest.getParameters()), rpcRequest.getRequestId());
        } catch (NoSuchMethodException e) {
            return RpcResponse.fail(ResponseCode.METHOD_NOT_FOUND, rpcRequest.getRequestId());
        }
    }
}
