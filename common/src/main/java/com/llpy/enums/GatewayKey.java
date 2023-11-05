package com.llpy.enums;


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
