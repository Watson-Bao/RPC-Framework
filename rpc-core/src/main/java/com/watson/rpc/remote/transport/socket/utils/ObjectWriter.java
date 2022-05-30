package com.watson.rpc.remote.transport.socket.utils;

import com.watson.rpc.enume.PackageType;
import com.watson.rpc.enume.SerializerEnum;
import com.watson.rpc.extension.ExtensionLoader;
import com.watson.rpc.remote.dto.RpcRequest;
import com.watson.rpc.serializer.Serializer;
import lombok.extern.slf4j.Slf4j;

import java.io.IOException;
import java.io.OutputStream;

/**
 * @author watson
 */
@Slf4j
public class ObjectWriter {
    private static final int MAGIC_NUMBER = 0xCAFEBABE;

    public static void writeObject(OutputStream outputStream, Object object, byte serializerCode) throws IOException {

        outputStream.write(intToBytes(MAGIC_NUMBER));
        if (object instanceof RpcRequest) {
            outputStream.write(intToBytes(PackageType.REQUEST_PACK.getCode()));
        } else {
            outputStream.write(intToBytes(PackageType.RESPONSE_PACK.getCode()));
        }
        outputStream.write(serializerCode);
        String codecName = SerializerEnum.getName(serializerCode);
        Serializer serializer = ExtensionLoader.getExtensionLoader(Serializer.class).getExtension(codecName);
        byte[] bytes = serializer.serialize(object);
        outputStream.write(intToBytes(bytes.length));
        outputStream.write(bytes);
        outputStream.flush();

    }

    private static byte[] intToBytes(int value) {
        byte[] src = new byte[4];
        src[0] = (byte) ((value >> 24) & 0xFF);
        src[1] = (byte) ((value >> 16) & 0xFF);
        src[2] = (byte) ((value >> 8) & 0xFF);
        src[3] = (byte) (value & 0xFF);
        return src;
    }
}
