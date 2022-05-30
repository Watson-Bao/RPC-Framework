package com.watson.rpc.remote.transport;

import com.watson.rpc.config.RpcServiceConfig;
import com.watson.rpc.serializer.Serializer;

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
     * @param serializer
     */
    void setSerializer(Serializer serializer);

    /**
     * 服务器端发布服务
     *
     * @param rpcServiceConfig
     */
    void registerService(RpcServiceConfig rpcServiceConfig);
}

