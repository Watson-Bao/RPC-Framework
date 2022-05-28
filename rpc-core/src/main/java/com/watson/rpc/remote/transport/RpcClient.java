package com.watson.rpc.remote.transport;

import com.watson.rpc.remote.to.RpcRequest;
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
