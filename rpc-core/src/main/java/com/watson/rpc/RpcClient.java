package com.watson.rpc;

import com.watson.rpc.entity.RpcRequest;

/**
 * 客户端类通用接口
 * @author watson
 */
public interface RpcClient {
    /**客户端发送请求
     * @param rpcRequest
     * @return
     */
    Object sendRequest(RpcRequest rpcRequest);
}
