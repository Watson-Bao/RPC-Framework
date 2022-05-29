package com.watson.test;

import com.watson.rpc.api.HelloObject;
import com.watson.rpc.api.HelloService;
import com.watson.rpc.config.RpcServiceConfig;
import com.watson.rpc.proxy.RpcClientProxy;
import com.watson.rpc.remote.transport.RpcClient;
import com.watson.rpc.remote.transport.socket.client.SocketRpcClient;

/**
 * 测试用消费者（客户端）
 *
 * @author watson
 */
public class SocketTestClient {
    public static void main(String[] args) {
        RpcClient client = new SocketRpcClient();
        RpcServiceConfig rpcServiceConfig = RpcServiceConfig.builder().group("socket").version("version2").build();
        RpcClientProxy rpcClientProxy = new RpcClientProxy(client, rpcServiceConfig);
        HelloService helloService = rpcClientProxy.getProxy(HelloService.class);

//        HelloObject object = new HelloObject(121, "This is a message");
//        String res = helloService.hello(object, "Socket---");
//        System.out.println(res);

        long s = System.currentTimeMillis();
        for (int i = 0; i < 50; i++) {
            String des = helloService.hello(new HelloObject(i, "This is a message~~~" + i), "Socket---");
            System.out.println(des);
        }
        long e = System.currentTimeMillis();
        System.out.println(e - s);
    }
}
