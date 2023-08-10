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
    // 业务异常
    public static CodeMsg USER_NOT_EXIST = new CodeMsg(500103, "用户不存在");
    public static CodeMsg USER_EXIST = new CodeMsg(500104, "用户名已存在");
    public static CodeMsg NOT_FIND_DATA = new CodeMsg(500106, "查找不到对应数据");
    public static CodeMsg USER_PASS_ERROR = new CodeMsg(500107, "密码错误!");
    public static CodeMsg UPLOAD_IMG_ERROR = new CodeMsg(500108, "图片上传失败");
    public static CodeMsg USER_EMAIL_ERROR = new CodeMsg(500109, "邮件发送失败");
    public static CodeMsg USER_EMAIL_CODE_ERROR = new CodeMsg(500109, "邮件验证码错误");
//    public static CodeMsg DATA_INSERT_ERROR = new CodeMsg(500108, "数据添加失败");
//    public static CodeMsg DATA_DELETE_ERROR = new CodeMsg(500109, "数据删除失败");
//    public static CodeMsg DATA_UPDATE_ERROR = new CodeMsg(500110, "数据更新失败");


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
