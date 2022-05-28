package com.watson.test;

import com.watson.rpc.api.HelloObject;
import com.watson.rpc.api.HelloService;
import com.watson.rpc.config.RpcServiceConfig;
import com.watson.rpc.proxy.RpcClientProxy;
import com.watson.rpc.remote.transport.RpcClient;
import com.watson.rpc.remote.transport.netty.client.NettyRpcClient;
import com.watson.rpc.serializer.Hessian2Serializer;
import com.watson.rpc.serializer.JsonSerializer;
import com.watson.rpc.serializer.KryoSerializer;


/**
 * 测试用Netty消费者
 *
 * @author watson
 */
public class NettyTestClient {
    public static void main(String[] args) {
        RpcClient client = new NettyRpcClient();
        client.setSerializer(new JsonSerializer());
        RpcServiceConfig rpcServiceConfig = RpcServiceConfig.builder().group("netty").version("version1").build();
        RpcClientProxy rpcClientProxy = new RpcClientProxy(client, rpcServiceConfig);
        HelloService helloService = rpcClientProxy.getProxy(HelloService.class);
        HelloObject object = new HelloObject(12, "This is a message");
        String res = helloService.hello(object, "Netty---");
        System.out.println(res);


    }
}
