package com.watson.rpc.registry.nacos;

import com.alibaba.nacos.api.exception.NacosException;
import com.alibaba.nacos.api.naming.pojo.Instance;
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
            Instance instance = instances.get(0);
            log.info("成功找到服务地址:[{}:{}]", instance.getIp(), instance.getPort());
            return new InetSocketAddress(instance.getIp(), instance.getPort());
        } catch (NacosException e) {
            log.error("获取服务时有错误发生:", e);
        }
        return null;
    }
}
