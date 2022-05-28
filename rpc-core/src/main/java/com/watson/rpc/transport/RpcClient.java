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
     * 客户端发送请求并获取结果
     *
     * @param rpcRequest
     * @return
     */
    Object sendRpcRequest(RpcRequest rpcRequest);

    /**
     * 设置序列化器
     *
     * @param serializer
     */
    void setSerializer(CommonSerializer serializer);
}
