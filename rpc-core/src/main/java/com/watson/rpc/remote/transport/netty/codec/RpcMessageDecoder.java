package com.watson.rpc.remote.transport.netty.codec;

import com.watson.rpc.enume.RpcError;
import com.watson.rpc.exception.RpcException;
import com.watson.rpc.remote.constant.RpcConstants;
import com.watson.rpc.remote.dto.RpcMessage;
import com.watson.rpc.remote.dto.RpcRequest;
import com.watson.rpc.remote.dto.RpcResponse;
import com.watson.rpc.serializer.CommonSerializer;
import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.LengthFieldBasedFrameDecoder;
import lombok.extern.slf4j.Slf4j;

/**
 * @author watson
 */
@Slf4j
public class RpcMessageDecoder extends LengthFieldBasedFrameDecoder {
    public RpcMessageDecoder() {
        // default is 8M
        this(RpcConstants.MAX_FRAME_LENGTH);
    }

    public RpcMessageDecoder(int maxFrameLength) {
    /*
        int maxFrameLength,
        int lengthFieldOffset,  magic code is 4B, and version is 1B, and then FullLength. so value is 5
        int lengthFieldLength,  FullLength is int(4B). so values is 4
        int lengthAdjustment,   FullLength include all data and read 9 bytes before, so the left length is (FullLength-9). so values is -9
        int initialBytesToStrip we will check magic code and version self, so do not strip any bytes. so values is 0
        */
        super(maxFrameLength, 5, 4, -9, 0);
    }

    @Override
    protected Object decode(ChannelHandlerContext ctx, ByteBuf in) throws Exception {
        Object decoded = super.decode(ctx, in);
        if (decoded instanceof ByteBuf) {
            ByteBuf frame = (ByteBuf) decoded;
            if (frame.readableBytes() >= RpcConstants.MIN_TOTAL_LENGTH) {
                try {
                    return decodeFrame(frame);
                } catch (Exception e) {
                    log.error("Decode frame error!", e);
                    throw e;
                } finally {
                    frame.release();
                }
            }

        }
        return decoded;
    }


    private Object decodeFrame(ByteBuf in) throws Exception {
        //读取前4个magic比对一下
        int len = RpcConstants.MAGIC_NUMBER.length;
        byte[] tmp = new byte[len];
        in.readBytes(tmp);
        for (int i = 0; i < len; i++) {
            if (tmp[i] != RpcConstants.MAGIC_NUMBER[i]) {
                log.error("不识别的协议包");
                throw new RpcException(RpcError.UNKNOWN_PROTOCOL);
            }
        }
        byte version = in.readByte();
        if (version != RpcConstants.VERSION) {
            throw new RuntimeException("数据包版本不兼容" + version);
        }
        //数据帧总长度
        int fullLength = in.readInt();
        //消息类型
        byte messageType = in.readByte();
        //读取序列化类型
        byte codecType = in.readByte();
        int requestId = in.readInt();
        RpcMessage rpcMessage = new RpcMessage();
        rpcMessage.setMessageType(messageType);
        rpcMessage.setRequestId(requestId);
        rpcMessage.setCodec(codecType);
        if (messageType == RpcConstants.HEARTBEAT_REQUEST_TYPE) {
            rpcMessage.setData(RpcConstants.PING);
        } else if (messageType == RpcConstants.HEARTBEAT_RESPONSE_TYPE) {
            rpcMessage.setData(RpcConstants.PONG);
        } else {
            int bodyLength = fullLength - RpcConstants.HEAD_LENGTH;
            if (bodyLength > 0) {
                byte[] messageBody = new byte[bodyLength];
                in.readBytes(messageBody);
                CommonSerializer serializer = CommonSerializer.getByCode(rpcMessage.getCodec());
                if (messageType == RpcConstants.REQUEST_TYPE) {
                    RpcRequest tmpValue = serializer.deserialize(messageBody, RpcRequest.class);
                    rpcMessage.setData(tmpValue);
                } else {
                    RpcResponse tmpValue = serializer.deserialize(messageBody, RpcResponse.class);
                    rpcMessage.setData(tmpValue);
                }
            }
        }
        return rpcMessage;

    }
}
