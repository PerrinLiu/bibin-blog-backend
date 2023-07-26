package com.llpy.enums;


public enum RedisKeyEnum {


    //
    REDIS_KEY_IMG_UUID_CODE("verUUidCode:", 60*2, "图片验证码"),
    //
    REDIS_KEY_IMG_BASE("baseImg", 60, "base64图片"),
    REDIS_KEY_IMG_TYPE("imgType", 60, "t图片类型"),
    REDIS_LIST_PROFIT("profit", 60, "利润表"),
    REDIS_LIST_BS("bs", 60, "利润表"),
    //
    REDIS_KEY_IMG_UUID_CODE_HEADER("verUUidCode", 60, "图片验证码唯一uuid放在header里"),
    REDIS_KEY_DEVICE_STATUS("device@",60*60*15,"设备在线状态"),
    REDIS_KEY_DEIVCE_LIST("deviceList:",60*60*24*30,"当前websocket设备列表"),
    //
    REDIS_KEY_USER_INFO("userInfo:", 8*60*60, "用户登录"),
    //
    REDIS_KEY_USER_ID("userId:",  8*60*60, "用户登录"),
    //
    REDIS_KEY_USER_HEADER_CODE("userHeader",-1,"用户信息Token"),
    //
    REDIS_KEY_USER_LIST_INFO("userList:", 60 * 60*3+2, "用户登录，限制登录次数"),
    REDIS_KEY_HEADER_INFO("X-Token", 60 * 60*15, "给前端的header"),

    REDIS_USER_P_STATUS_INFO("profitStatus:",-1,"用户利润表状态信息,实时更新"),
    REDIS_USER_B_STATUS_INFO("BsStatus:",-1,"用户资产表状态信息,实时更新"),
    REDIS_ACL_MODULE_MAP("aclModuleMap:",-1,"权限模块 Map"),
    REDIS_ACL_MODULE_OBJ("aclModuleObj:",-1,"权限模块 对象"),
    REDIS_ACL_MAP("aclMap:",-1,"权限点模块 Map"),
    REDIS_ACL_OBJ("aclObj:",-1,"权限点模块 对象"),
    REDIS_ROLE_STR("role:",-1,"普通角色 "),
    REDIS_ADMIN_ROLE_STR("adminRole:",-1,"管理员角色 "),
    REDIS_USER_ROLES_STR("userRoles:",-1,"用户和角色关系 "),
    REDIS_ROLE_ACLS_MAP("roleAcls:",-1,"角色和权限关系 "),
    ;
    private final String key;
    private final int expireTime;
    private final String keyInfo;


    RedisKeyEnum(String key, int expireTime, String keyInfo) {
        this.key = key;
        this.expireTime = expireTime;
        this.keyInfo = keyInfo;
    }


    public String getKey() {
        return key;
    }

    public int getExpireTime() {
        return expireTime;
    }

    public String getKeyInfo() {
        return keyInfo;
    }
}
