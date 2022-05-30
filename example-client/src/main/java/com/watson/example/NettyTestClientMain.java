package com.watson.example;

import com.watson.rpc.api.HelloObject;
import com.watson.rpc.api.HelloService;
import com.watson.rpc.config.RpcServiceConfig;
import com.watson.rpc.enume.SerializerEnum;
import com.watson.rpc.proxy.RpcClientProxy;
import com.watson.rpc.remote.transport.RpcClient;
import com.watson.rpc.remote.transport.netty.client.NettyRpcClient;


/**
 * 测试用Netty消费者
 *
 * @author watson
 */
public class NettyTestClientMain {
    public static void main(String[] args) {
        RpcClient client = new NettyRpcClient(SerializerEnum.KRYO.getCode());
        RpcServiceConfig rpcServiceConfig = RpcServiceConfig.builder().group("netty").version("version1").build();
        RpcClientProxy rpcClientProxy = new RpcClientProxy(client, rpcServiceConfig);
        HelloService helloService = rpcClientProxy.getProxy(HelloService.class);

//        HelloObject object = new HelloObject(12, "This is a message");
//        String res = helloService.hello(object, "Netty---");
//        System.out.println(res);

        long s = System.currentTimeMillis();
        for (int i = 0; i < 10; i++) {
            String des = helloService.hello(new HelloObject(i, "This is a message~~~" + i), "Netty---");
            System.out.println(des);
        }
        long e = System.currentTimeMillis();
        System.out.println(e - s);


    }
}
