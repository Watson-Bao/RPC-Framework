package com.watson.rpc.remote.to;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

/**
 * 消费者向提供者发送的请求对象
 *
 * @author watson
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class RpcRequest implements Serializable {
    /**
     * 请求号
     */
    private String requestId;

    /**
     * 待调用接口名称
     */
    private String interfaceName;

    /**
     * 待调用方法名称
     */
    private String methodName;

    /**
     * 调用方法的参数
     */
    private Object[] parameters;

    /**
     * 调用方法的参数类型
     */
    private Class<?>[] paramTypes;

    /**
     * 版本号
     */
    private String version;
    /**
     * 分组
     */
    private String group;

    public String getRpcServiceName() {
        return this.getInterfaceName() + "-" + this.getGroup() + "-" + this.getVersion();
    }
}
