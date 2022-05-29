package com.watson.rpc.remote.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author watson
 */
@AllArgsConstructor
@NoArgsConstructor
@Data
public class RpcMessage {
    /**
     * 消息类型
     */
    private byte messageType;

    /**
     * 序列化类型
     */
    private byte codec;

    /**
     * 请求id
     */
    private int requestId;

    /**
     * 数据内容
     */
    private Object data;
}
