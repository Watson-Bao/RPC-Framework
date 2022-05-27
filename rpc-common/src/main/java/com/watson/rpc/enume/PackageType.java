package com.watson.rpc.enume;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * @author watson
 */
@AllArgsConstructor
@Getter
public enum PackageType {
    /**
     * 数据包类型
     */
    REQUEST_PACK(0),
    RESPONSE_PACK(1);

    private final int code;
}
