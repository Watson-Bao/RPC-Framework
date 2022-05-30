package com.watson.rpc.remote.transport;

import com.watson.rpc.config.RpcServiceConfig;

/**
 * 服务器类通用接口
 *
 * @author watson
 */
public interface RpcServer {
    /**
     * 服务器端开始监听请求
     */
    void start();

    /**
     * 设置序列化器
     *
     * @param serializerCode
     */
    void setSerializer(byte serializerCode);

    /**
     * 服务器端发布服务
     *
     * @param rpcServiceConfig
     */
    void registerService(RpcServiceConfig rpcServiceConfig);
}

