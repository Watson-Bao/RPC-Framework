package com.watson.rpc.remote.transport.socket.client;

import com.watson.rpc.enume.RpcError;
import com.watson.rpc.exception.RpcException;
import com.watson.rpc.registry.ServiceDiscovery;
import com.watson.rpc.registry.nacos.NacosServiceDiscovery;
import com.watson.rpc.remote.dto.RpcRequest;
import com.watson.rpc.remote.transport.RpcClient;
import com.watson.rpc.remote.transport.socket.utils.ObjectReader;
import com.watson.rpc.remote.transport.socket.utils.ObjectWriter;
import com.watson.rpc.serializer.CommonSerializer;
import com.watson.rpc.serializer.Hessian2Serializer;
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
    private CommonSerializer serializer;

    public SocketRpcClient() {
        this(new Hessian2Serializer());
    }

    public SocketRpcClient(CommonSerializer serializer) {
        this.serviceDiscovery = new NacosServiceDiscovery();
        this.serializer = serializer;
    }

    @Override
    public Object sendRpcRequest(RpcRequest rpcRequest) {
        if (serializer == null) {
            log.error("未设置序列化器");
            throw new RpcException(RpcError.SERIALIZER_NOT_FOUND);
        }
        InetSocketAddress inetSocketAddress = serviceDiscovery.lookupService(rpcRequest);
        try (Socket socket = new Socket()) {
            socket.connect(inetSocketAddress);
            OutputStream outputStream = socket.getOutputStream();
            InputStream inputStream = socket.getInputStream();
            ObjectWriter.writeObject(outputStream, rpcRequest, serializer);
            return ObjectReader.readObject(inputStream);
        } catch (IOException e) {
            log.error("调用时有错误发生：", e);
            throw new RpcException("服务调用失败: ", e);
        }
    }

    /**
     * 设置序列化器
     *
     * @param serializer
     */
    @Override
    public void setSerializer(CommonSerializer serializer) {
        this.serializer = serializer;
    }
}
