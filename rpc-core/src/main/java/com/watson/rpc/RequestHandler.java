package com.watson.rpc;

import com.watson.rpc.entity.RpcRequest;
import com.watson.rpc.entity.RpcResponse;
import com.watson.rpc.enume.ResponseCode;
import lombok.extern.slf4j.Slf4j;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.net.Socket;

/**
 * 进行过程调用的处理器
 * @author watson
 */
@Slf4j
public class RequestHandler{
    public Object handle(RpcRequest rpcRequest, Object service){
        Object result = null;

        try {
            result = invokeTargetMethod(rpcRequest, service);
            log.info("服务:{} 成功调用方法:{}", rpcRequest.getInterfaceName(), rpcRequest.getMethodName());
        } catch (IllegalAccessException | InvocationTargetException e) {
            log.error("调用或发送时有错误发生：", e);
        }
        return result;
    }

    /**
     * 通过传输过来的request中方法的相关信息反射出目标方法并执行
     * @param rpcRequest
     * @param service
     * @return
     * @throws IllegalAccessException
     * @throws InvocationTargetException
     */
    private Object invokeTargetMethod(RpcRequest rpcRequest, Object service) throws IllegalAccessException, InvocationTargetException {
        Method method;
        try {
            method = service.getClass().getMethod(rpcRequest.getMethodName(), rpcRequest.getParamTypes());
        } catch (NoSuchMethodException e) {
            return RpcResponse.fail(ResponseCode.METHOD_NOT_FOUND);
        }
        return method.invoke(service, rpcRequest.getParameters());
    }
}
