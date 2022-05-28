package com.watson.rpc.provider;

import com.watson.rpc.config.RpcServiceConfig;

/**
 * 保存和提供服务实例对象
 * @author watson
 */
public interface ServiceProvider {

    /**
     * 将一个服务baocun进注册表
     *
     * @param <T>
     * @param rpcServiceConfig
     */
    void addService(RpcServiceConfig rpcServiceConfig);


    /**
     * 根据服务名称获取服务实体
     * @param rpcServiceName
     * @return
     */
    Object getService(String rpcServiceName);

    /**
     * 服务器端发布服务
     * @param rpcServiceConfig
     * @param port
     */
    public void publishService(RpcServiceConfig rpcServiceConfig, int port);
}
