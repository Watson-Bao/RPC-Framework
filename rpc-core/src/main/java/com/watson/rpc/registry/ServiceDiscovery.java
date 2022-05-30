package com.watson.rpc.registry;

import com.watson.rpc.extension.SPI;
import com.watson.rpc.remote.dto.RpcRequest;

import java.net.InetSocketAddress;

/**
 * @author watson
 */
@SPI
public interface ServiceDiscovery {
    /**
     * 根据服务名称查找服务实体连接地址
     *
     * @param rpcRequest rpc请求信息
     * @return 服务实体连接地址
     */
    InetSocketAddress lookupService(RpcRequest rpcRequest);
}
