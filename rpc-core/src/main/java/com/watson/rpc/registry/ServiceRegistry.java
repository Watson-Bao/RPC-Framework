package com.watson.rpc.registry;

import com.watson.rpc.config.RpcServiceConfig;
import com.watson.rpc.extension.SPI;

import java.net.InetSocketAddress;

/**
 * 服务注册接口
 *
 * @author watson
 */
@SPI
public interface ServiceRegistry {
    /**
     * 将一个服务注册进注册表
     *
     * @param rpcServiceConfig  rpc服务相关信息
     * @param inetSocketAddress 提供服务的地址
     */
    void register(RpcServiceConfig rpcServiceConfig, InetSocketAddress inetSocketAddress);
}
