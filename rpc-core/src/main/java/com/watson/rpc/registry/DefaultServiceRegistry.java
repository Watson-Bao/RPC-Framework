package com.watson.rpc.registry;

import com.watson.rpc.enume.RpcError;
import com.watson.rpc.exception.RpcException;
import lombok.extern.slf4j.Slf4j;

import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

/**
 * 默认的服务注册表
 * @author watson
 */
@Slf4j
public class DefaultServiceRegistry implements ServiceRegistry{
    private final Map<String, Object> serviceMap = new ConcurrentHashMap<>();
    private final Set<String> registeredService = ConcurrentHashMap.newKeySet();
    /**
     * 将一个服务注册进注册表
     *
     * @param service 待注册的服务实体
     */
    @Override
    public <T> void register(T service) {
        String serviceName = service.getClass().getCanonicalName();
        if(registeredService.contains(serviceName)) {
            return;
        }
        registeredService.add(serviceName);
        Class<?>[] interfaces = service.getClass().getInterfaces();
        if(interfaces.length == 0) {
            throw new RpcException(RpcError.SERVICE_NOT_IMPLEMENT_ANY_INTERFACE);
        }
        for(Class<?> i : interfaces) {
            serviceMap.put(i.getCanonicalName(), service);
            log.info("向接口: {} 注册服务: {}", interfaces, serviceName);
        }
    }

    /**
     * 根据服务名称获取服务实体
     *
     * @param serviceName 服务名称
     * @return 服务实体
     */
    @Override
    public Object getService(String serviceName) {
        Object service = serviceMap.get(serviceName);
        if(service == null) {
            throw new RpcException(RpcError.SERVICE_NOT_FOUND);
        }
        return service;
    }
}
