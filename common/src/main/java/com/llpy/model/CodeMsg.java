package com.llpy.model;

/**
 * @program: hd
 * @description: 错误CODE和MSG
 * @author: XuChao
 * @create: 2019-12-24 14:57
 **/
public class CodeMsg {

    private int retCode;
    private String message;

    // 按照模块定义CodeMsg
     public static CodeMsg API_FAILED = new CodeMsg(10004, "平台接口调用失败");
    // 通用异常
    public static CodeMsg SUCCESS = new CodeMsg(0, "操作成功");
    public static CodeMsg SERVER_EXCEPTION = new CodeMsg(500100, "服务端异常");
    public static CodeMsg PARAMETER_ISNULL = new CodeMsg(500101, "输入参数为空");
    public static CodeMsg PARAMETER_ERROR = new CodeMsg(500102, "输入参数异常");
    public static CodeMsg RESIGN_EMPERROR = new CodeMsg(500001, "离职员工账号已被停用");
    // 业务异常
    public static CodeMsg USER_NOT_EXIST = new CodeMsg(500103, "用户不存在");
    public static CodeMsg NOT_FIND_DATA = new CodeMsg(500106, "查找不到对应数据");
    public static CodeMsg USER_PASS_ERROR = new CodeMsg(500107, "密码错误!");
    public static CodeMsg FILE_CANNOT_EMPTY = new CodeMsg(500026, "文件不能为空");
    public static CodeMsg FILE_FORMATTER_ERROR = new CodeMsg(500027, "文件表头不匹配");
    public static CodeMsg SOURCE_SCHOOL_BATCH_IMPORT_ERROR = new CodeMsg(500028, "导入模板有误，请检查！");
    public static CodeMsg SOURCE_SCHOOL_BATCH_IMPORT_ERROR_DATA = new CodeMsg(500029, "临时表复制错误，请检查！");
    public static CodeMsg DATA_INSERT_ERROR = new CodeMsg(500108, "数据添加失败");
    public static CodeMsg DATA_DELETE_ERROR = new CodeMsg(500109, "数据删除失败");
    public static CodeMsg DATA_UPDATE_ERROR = new CodeMsg(500110, "数据更新失败");
    public static CodeMsg DIC_CODE_REPEAT = new CodeMsg(500111, "数据字典编码/名称不能重复");
    public static CodeMsg DIC_DELETE_ERROR = new CodeMsg(500112, "该数据字典有子数据，无法删除");
    public static CodeMsg USER_ACCOUNT_REPEAT = new CodeMsg(500117, "用户帐号不能重复");
    public static CodeMsg ROLE_UPDATE_FAIL=new CodeMsg(500131,"角色更新失败");
    public static CodeMsg ROLE_NAME_REPEAT=new CodeMsg(500132,"角色名称不能重复");
    public static CodeMsg LACK_OF_PARAM = new CodeMsg(500134, "缺少参数");
    public static CodeMsg USER_ROLE = new CodeMsg(60007,"请要求管理员分配角色，登录失败");
    public static CodeMsg PHONE_TYPE_ERROR = new CodeMsg(60017,"手机号码格式错误,非大陆号码请联系管理员");
    public static CodeMsg UNLAWFUL_SYMBOL = new CodeMsg(60012,"请勿输入非法字符!!!");


    public CodeMsg(int retCode, String message) {
        this.retCode = retCode;
        this.message = message;
    }

    public int getRetCode() {
        return retCode;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

}
