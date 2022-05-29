package com.watson.rpc.remote.constant;

import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;

/**
 * @author watson
 */
public class RpcConstants {
    /**
     * 魔法数 检验 RpcMessage
     * my rpc
     */
    public static final byte[] MAGIC_NUMBER = {(byte) 'm', (byte) 'r', (byte) 'p', (byte) 'c'};


    /**
     * 字符编码
     */
    public static final Charset DEFAULT_CHARSET = StandardCharsets.UTF_8;

    /**
     * 版本信息
     */
    public static final byte VERSION = 1;
    /**
     * 最短有效帧长
     */
    public static final byte MIN_TOTAL_LENGTH = 15;

    /**
     * 请求包
     */
    public static final byte REQUEST_TYPE = 1;

    /**
     * 响应包
     */
    public static final byte RESPONSE_TYPE = 2;

    /**
     * 心跳 ping
     */
    public static final byte HEARTBEAT_REQUEST_TYPE = 3;

    /**
     * 心跳 pong
     */
    public static final byte HEARTBEAT_RESPONSE_TYPE = 4;

    /**
     * 数据帧头长度
     */
    public static final int HEAD_LENGTH = 15;

    /**
     *
     */
    public static final String PING = "ping";


    /**
     *
     */
    public static final String PONG = "pong";

    /**
     * 最大数据帧长8M
     */
    public static final int MAX_FRAME_LENGTH = 8 * 1024 * 1024;
}
