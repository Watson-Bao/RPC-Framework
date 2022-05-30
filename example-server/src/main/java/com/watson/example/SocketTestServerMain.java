package com.watson.example;

import com.watson.rpc.api.HelloService;
import com.watson.rpc.config.RpcServiceConfig;
import com.watson.rpc.enume.SerializerEnum;
import com.watson.rpc.remote.transport.RpcServer;
import com.watson.rpc.remote.transport.socket.server.SocketRpcServer;

/**
 * @author watson
 */
public class SocketTestServerMain {
    public static void main(String[] args) {
        HelloService helloService = new HelloServiceImpl2();
        RpcServer socketRpcServer = new SocketRpcServer(9000, SerializerEnum.JSON.getCode());
        RpcServiceConfig rpcServiceConfig = RpcServiceConfig.builder()
                .group("socket").version("version2").service(helloService).build();
        socketRpcServer.registerService(rpcServiceConfig);
        socketRpcServer.start();
    }
}
