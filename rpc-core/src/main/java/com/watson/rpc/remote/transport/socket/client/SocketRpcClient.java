package com.watson.rpc.remote.transport.socket.client;

import com.watson.rpc.enume.RpcError;
import com.watson.rpc.enume.SerializerEnum;
import com.watson.rpc.exception.RpcException;
import com.watson.rpc.extension.ExtensionLoader;
import com.watson.rpc.registry.ServiceDiscovery;
import com.watson.rpc.remote.dto.RpcRequest;
import com.watson.rpc.remote.transport.RpcClient;
import com.watson.rpc.remote.transport.socket.utils.ObjectReader;
import com.watson.rpc.remote.transport.socket.utils.ObjectWriter;
import com.watson.rpc.serializer.Hessian2Serializer;
import com.watson.rpc.serializer.Serializer;
import lombok.extern.slf4j.Slf4j;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.InetSocketAddress;
import java.net.Socket;

/**
 * 远程方法调用的消费者（客户端）
 *
 * @author watson
 */
@Slf4j
public class SocketRpcClient implements RpcClient {

    private final ServiceDiscovery serviceDiscovery;
    private byte serializerCode;

    public SocketRpcClient() {
        this(SerializerEnum.HESSIAN2.getCode());
    }

    public SocketRpcClient(byte serializerCode) {
        this.serviceDiscovery = ExtensionLoader.getExtensionLoader(ServiceDiscovery.class).getExtension("nacos");
        this.serializerCode = serializerCode;
    }

    @Override
    public Object sendRpcRequest(RpcRequest rpcRequest) {

        InetSocketAddress inetSocketAddress = serviceDiscovery.lookupService(rpcRequest);
        try (Socket socket = new Socket()) {
            socket.connect(inetSocketAddress);
            OutputStream outputStream = socket.getOutputStream();
            InputStream inputStream = socket.getInputStream();
            ObjectWriter.writeObject(outputStream, rpcRequest, serializerCode);
            return ObjectReader.readObject(inputStream);
        } catch (IOException e) {
            log.error("调用时有错误发生：", e);
            throw new RpcException("服务调用失败: ", e);
        }
    }


    @Override
    public void setSerializer(byte serializerCode) {
        this.serializerCode = serializerCode;
    }
}
