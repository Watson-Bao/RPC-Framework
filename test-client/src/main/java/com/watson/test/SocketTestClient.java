package com.watson.test;

import com.watson.rpc.RpcClient;
import com.watson.rpc.RpcClientProxy;
import com.watson.rpc.api.HelloObject;
import com.watson.rpc.api.HelloService;
import com.watson.rpc.serializer.KryoSerializer;
import com.watson.rpc.socket.client.SocketClient;

/**
 * 测试用消费者（客户端）
 *
 * @author watson
 */
public class SocketTestClient {
    public static void main(String[] args) {
        RpcClient client = new SocketClient("127.0.0.1", 9000);
        client.setSerializer(new KryoSerializer());
        RpcClientProxy proxy = new RpcClientProxy(client);
        HelloService helloService = proxy.getProxy(HelloService.class);
        HelloObject object = new HelloObject(121, "This is a message");
        String res = helloService.hello(object, "1111111111");
        System.out.println(res);
    }
}
