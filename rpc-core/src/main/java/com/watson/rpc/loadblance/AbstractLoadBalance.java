package com.watson.rpc.loadblance;

import com.alibaba.nacos.api.naming.pojo.Instance;

import java.util.List;

/**
 * @author watson
 */
public abstract class AbstractLoadBalance implements LoadBalance {
    @Override
    public Instance selectServiceAddress(List<Instance> instances) {
        if (instances == null || instances.size() == 0) {
            return null;
        }
        if (instances.size() == 1) {
            return instances.get(0);
        }
        return doSelect(instances);
    }

    /**
     * 选择一个服务实例（包含地址信息）
     *
     * @param instances
     * @return 选中的服务地址
     */
    protected abstract Instance doSelect(List<Instance> instances);
}
