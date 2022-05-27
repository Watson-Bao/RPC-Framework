package com.watson.rpc.transport;

import com.watson.rpc.entity.RpcRequest;
import com.watson.rpc.serializer.CommonSerializer;

/**
 * 客户端类通用接口
 *
 * @author watson
 */
public interface RpcClient {
    /**
     * 客户端发送请求
     *
     * @param rpcRequest
     * @return
     */
    Object sendRequest(RpcRequest rpcRequest);

    /**
     * 设置序列化器
     *
     * @param serializer
     */
    void setSerializer(CommonSerializer serializer);
}
