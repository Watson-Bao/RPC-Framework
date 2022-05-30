package com.watson.rpc.remote.transport;

import com.watson.rpc.extension.SPI;
import com.watson.rpc.remote.dto.RpcRequest;

/**
 * 客户端类通用接口
 *
 * @author watson
 */
@SPI
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
     * @param serializerCode
     */
    void setSerializer(byte serializerCode);
}
