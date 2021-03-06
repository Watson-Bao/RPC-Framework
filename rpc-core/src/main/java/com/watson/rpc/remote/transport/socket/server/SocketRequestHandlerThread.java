package com.watson.rpc.remote.transport.socket.server;

import com.watson.rpc.factory.SingletonFactory;
import com.watson.rpc.remote.dto.RpcRequest;
import com.watson.rpc.remote.dto.RpcResponse;
import com.watson.rpc.remote.handler.RpcRequestHandler;
import com.watson.rpc.remote.transport.socket.utils.ObjectReader;
import com.watson.rpc.remote.transport.socket.utils.ObjectWriter;
import lombok.extern.slf4j.Slf4j;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.Socket;

/**
 * 处理RpcRequest的工作线程
 *
 * @author watson
 */
@Slf4j
public class SocketRequestHandlerThread implements Runnable {
    private final Socket socket;
    private final RpcRequestHandler rpcRequestHandler;
    private final byte serializerCode;

    public SocketRequestHandlerThread(Socket socket, byte serializerCode) {
        this.socket = socket;
        this.rpcRequestHandler = SingletonFactory.getInstance(RpcRequestHandler.class);
        this.serializerCode = serializerCode;
    }

    @Override
    public void run() {
        log.info("server handle message from client by thread: [{}]", Thread.currentThread().getName());
        try (InputStream inputStream = socket.getInputStream();
             OutputStream outputStream = socket.getOutputStream()) {
            RpcRequest rpcRequest = (RpcRequest) ObjectReader.readObject(inputStream);
            RpcResponse<Object> response = rpcRequestHandler.handle(rpcRequest);
            ObjectWriter.writeObject(outputStream, response, serializerCode);
        } catch (IOException e) {
            log.error("调用或发送时有错误发生：", e);
        }
    }
}
