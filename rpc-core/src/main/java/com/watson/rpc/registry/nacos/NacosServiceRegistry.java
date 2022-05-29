package com.watson.rpc.registry.nacos;

import com.alibaba.nacos.api.exception.NacosException;
import com.watson.rpc.config.RpcServiceConfig;
import com.watson.rpc.enume.RpcError;
import com.watson.rpc.exception.RpcException;
import com.watson.rpc.registry.ServiceRegistry;
import com.watson.rpc.registry.nacos.utils.NacosUtil;
import lombok.extern.slf4j.Slf4j;

import java.net.InetSocketAddress;

/**
 * @author watson
 */
@Slf4j
public class NacosServiceRegistry implements ServiceRegistry {

    /**
     * 将一个服务注册进注册表
     *
     * @param rpcServiceConfig  rpc服务相关信息
     * @param inetSocketAddress 提供服务的地址
     */
    @Override
    public void register(RpcServiceConfig rpcServiceConfig, InetSocketAddress inetSocketAddress) {
        try {
            NacosUtil.registerService(rpcServiceConfig, inetSocketAddress);
        } catch (NacosException e) {
            log.error("注册服务时有错误发生:", e);
            throw new RpcException(RpcError.REGISTER_SERVICE_FAILED);
        }
    }

}
