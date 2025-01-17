package com.llpy.enums;


/**
 * 网关密钥
 *
 * @author llpy
 * @date 2024/07/04
 */
public enum GatewayKey {

    GATEWAY_KEY("gatewayKey", "xiaoKeAiXiangCaiDeMiYao");
    private final String key;
    private final String keyInfo;

    GatewayKey(String key, String keyInfo) {
        this.key = key;
        this.keyInfo = keyInfo;
    }


    public String getKey() {
        return key;
    }

    public String getKeyInfo() {
        return keyInfo;
    }

}
