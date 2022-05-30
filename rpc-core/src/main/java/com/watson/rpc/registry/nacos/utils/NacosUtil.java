package com.watson.rpc.registry.nacos.utils;

import com.alibaba.nacos.api.exception.NacosException;
import com.alibaba.nacos.api.naming.NamingFactory;
import com.alibaba.nacos.api.naming.NamingService;
import com.alibaba.nacos.api.naming.pojo.Instance;
import com.watson.rpc.config.RpcServiceConfig;
import com.watson.rpc.enume.RpcError;
import com.watson.rpc.exception.RpcException;
import com.watson.rpc.remote.dto.RpcRequest;
import lombok.extern.slf4j.Slf4j;

import java.net.InetSocketAddress;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * 管理Nacos连接等工具类
 *
 * @author watson
 */
@Slf4j
public class NacosUtil {
    private static final NamingService namingService;
    private static final Set<RpcServiceConfig> RPC_SERVICE_CONFIGS = new HashSet<>();
    private static final String SERVER_ADDR = "wslhost:8848";
    private static InetSocketAddress address;

    static {
        long sc = System.currentTimeMillis();
        namingService = getNacosNamingService();
        long ec = System.currentTimeMillis();
        log.info("连接到Nacos耗时：{}", ec-sc);

    }

    private static NamingService getNacosNamingService() {
        try {
            return NamingFactory.createNamingService(SERVER_ADDR);
        } catch (NacosException e) {
            log.error("连接到Nacos时有错误发生: ", e);
            throw new RpcException(RpcError.FAILED_TO_CONNECT_TO_SERVICE_REGISTRY);
        }
    }

    public static void registerService(RpcServiceConfig rpcServiceConfig, InetSocketAddress address) throws NacosException {

        long s1 = System.currentTimeMillis();
        namingService.registerInstance(rpcServiceConfig.getRpcServiceName(), rpcServiceConfig.getGroup(), address.getHostName(), address.getPort());
        long e1 = System.currentTimeMillis();
        log.info("服务注册耗时：{}", e1-s1);
        NacosUtil.address = address;
        RPC_SERVICE_CONFIGS.add(rpcServiceConfig);
    }

    public static List<Instance> getAllInstance(RpcRequest rpcRequest) throws NacosException {
        return namingService.getAllInstances(rpcRequest.getRpcServiceName(), rpcRequest.getGroup());

    }

    public static void clearRegistry() {
        if (!RPC_SERVICE_CONFIGS.isEmpty() && address != null) {
            String host = address.getHostName();
            int port = address.getPort();
            for (RpcServiceConfig rpcService : RPC_SERVICE_CONFIGS) {
                try {
                    namingService.deregisterInstance(rpcService.getRpcServiceName(), rpcService.getGroup(), host, port);
                } catch (NacosException e) {
                    log.error("注销服务 {} 失败", rpcService.getRpcServiceName(), e);
                }
            }
        }
    }
}
