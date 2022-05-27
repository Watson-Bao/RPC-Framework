package com.watson.rpc.provider;

/**
 * 保存和提供服务实例对象
 * @author watson
 */
public interface ServiceProvider {

    /**
     * 将一个服务baocun进注册表
     * @param service
     * @param <T>
     */
    <T> void addServiceProvider(T service);


    /**
     * 根据服务名称获取服务实体
     * @param serviceName
     * @return
     */
    Object getServiceProvider(String serviceName);
}
