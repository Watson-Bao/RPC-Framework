package com.watson.test.client;

import com.watson.rpc.api.HelloObject;
import com.watson.rpc.api.HelloService;
import com.watson.rpc.client.RpcClientProxy;

/**
 * 测试用消费者（客户端）
 * @author watson
 */
public class TestClient {
    public static void main(String[] args) {
        RpcClientProxy proxy = new RpcClientProxy("127.0.0.1", 9000);
        HelloService helloService = proxy.getProxy(HelloService.class);
        HelloObject object = new HelloObject(121, "This is a message");
        String res = helloService.hello(object);
        System.out.println(res);
    }
}
