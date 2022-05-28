package com.watson.rpc.config;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author watson
 */
@AllArgsConstructor
@NoArgsConstructor
@Data
@Builder
public class RpcServiceConfig {
    /**
     * service version
     */
    private String version = "";
    /**
     * when the interface has multiple implementation classes, distinguish by group
     */
    private String group = "";

    /**
     * target service
     */
    private Object service;

    public String getRpcServiceName() {
        return this.getInterfaceName() + "-" + this.getGroup() + "-" + this.getVersion();
    }

    public String getInterfaceName() {
        return this.service.getClass().getInterfaces()[0].getCanonicalName();
    }
}
