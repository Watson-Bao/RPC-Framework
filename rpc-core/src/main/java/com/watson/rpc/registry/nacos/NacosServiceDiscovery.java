package com.watson.rpc.registry.nacos;

import com.alibaba.nacos.api.exception.NacosException;
import com.alibaba.nacos.api.naming.pojo.Instance;
import com.watson.rpc.enume.RpcError;
import com.watson.rpc.exception.RpcException;
import com.watson.rpc.extension.ExtensionLoader;
import com.watson.rpc.loadbalance.LoadBalance;
import com.watson.rpc.registry.ServiceDiscovery;
import com.watson.rpc.registry.nacos.utils.NacosUtil;
import com.watson.rpc.remote.dto.RpcRequest;
import lombok.extern.slf4j.Slf4j;

import java.net.InetSocketAddress;
import java.util.List;

/**
 * @author watson
 */
@Slf4j
public class NacosServiceDiscovery implements ServiceDiscovery {
    private final LoadBalance loadBalance;

    public NacosServiceDiscovery() {
        this.loadBalance = ExtensionLoader.getExtensionLoader(LoadBalance.class).getExtension("loadBalance");
    }

    /**
     * 根据服务名称查找服务实体连接地址
     *
     * @param rpcRequest rpc请求信息
     * @return 服务实体连接地址
     */
    @Override
    public InetSocketAddress lookupService(RpcRequest rpcRequest) {
        try {
            List<Instance> instances = NacosUtil.getAllInstance(rpcRequest);
            if (instances != null && instances.size() > 0) {
                Instance instance = loadBalance.selectServiceAddress(instances);
                log.info("成功找到服务地址:[{}:{}]", instance.getIp(), instance.getPort());
                return new InetSocketAddress(instance.getIp(), instance.getPort());
            } else {
                throw new RpcException(RpcError.SERVICE_NOT_REGISTER);
            }

        } catch (NacosException e) {
            log.error("获取服务时有错误发生:", e);
        }
        return null;
    }
}
