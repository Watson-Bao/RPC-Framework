package com.watson.rpc.remote.handler;

import com.watson.rpc.enume.ResponseCode;
import com.watson.rpc.factory.SingletonFactory;
import com.watson.rpc.provider.ServiceProvider;
import com.watson.rpc.provider.ServiceProviderImpl;
import com.watson.rpc.remote.dto.RpcRequest;
import com.watson.rpc.remote.dto.RpcResponse;
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
        Object service = serviceProvider.getService(rpcRequest.getRpcServiceName());
        return invokeTargetMethod(rpcRequest, service);
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
    private RpcResponse<Object> invokeTargetMethod(RpcRequest rpcRequest, Object service) {
        try {
            Method method = service.getClass().getMethod(rpcRequest.getMethodName(), rpcRequest.getParamTypes());
            log.info("服务:{} 成功调用方法:{}", rpcRequest.getInterfaceName(), rpcRequest.getMethodName());
            return RpcResponse.success(method.invoke(service, rpcRequest.getParameters()), rpcRequest.getRequestId());
        } catch (InvocationTargetException | NoSuchMethodException | IllegalAccessException e) {
            log.error("调用或发送时有错误发生：", e);
            return RpcResponse.fail(ResponseCode.METHOD_NOT_FOUND, rpcRequest.getRequestId());
        }

    }
}
