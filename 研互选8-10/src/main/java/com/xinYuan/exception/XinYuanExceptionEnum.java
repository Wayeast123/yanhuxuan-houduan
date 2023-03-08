package com.xinYuan.exception;

/**
 * 描述：     异常枚举 ，主要用于列举出各种异常，然后协助抛出异常（注意是协助，相当于把异常赋值给需要的对象）
 */
public enum XinYuanExceptionEnum {
    //业务异常
    NEED_ACCOUNT(10001, "账号不能为空"),
    NEED_PASSWORD(10002, "密码不能为空"),
    PASSWORD_TOO_SHORT(10003, "密码长度不能小于8位"),
    ACCOUNT_EXISTED(10004, "账号重复"),
    INSERT_FAILED(10005, "插入失败，请重试"),
    WRONG_PASSWORD(10006, "密码错误"),
    NEED_LOGIN(10007, "用户未登录"),
    UPDATE_FAILED(10008, "更新失败"),
    NEED_ADMIN(10009, "无管理员权限"),
    PARA_NOT_NULL(10010, "参数不能为空"),
    CREATE_FAILED(10011, "新增失败"),
    REQUEST_PARAM_ERROR(10012, "参数错误"),
    DELETE_FAILED(10013, "删除失败"),
    MKDIR_FAILED(10014, "文件夹创建失败"),
    UPLOAD_FAILED(10015, "图片上传失败"),
    NO_ENUM(10016, "未找到对应的枚举"),
    SCREEN_NOT_NULL(10017,"筛选条件不能为空"),
    NICK_TOO_LONG(10018,"昵称不能太长"),
    PASSWORD_EXISTED(10019, "密码重复"),
    NICK_EXISTED(10020, "昵称重复"),
    TEACHER_NULL(10021,"无对应学校相关专业的导师"),
    SCHOOLID_MAJORID_NULL(10022,"请选择学校或专业"),
    USERNAME_REPEAT(10023,"账户目前正处于审核状态"),
    MATERIAL_ERROR(10024,"提交审核材料失败"),
    RESUME_REPEAT(10025,"该用户已创建简历"),
    TUTOR_AUTHORITY(10026,"无导师权限"),
    NO_IDENTITY(10027,"无此身份"),
    NO_SENT_AUTHORITY(10028, "无发送简历权限"),
    NO_SENT_REPEAT(10029, "已经发送过，不能重复发送"),  //删了之后是可以再发的，描述还需改
    NO_CREATE_RESUME(10030, "还未创建个人简历"),
    NEED_NICK(10031, "昵称不能为空"),
    NEED_TELEPHONE(10032, "电话不能为空"),
    TELEPHONE_ERROR(10033, "电话号码不规范"),
    TELEPHONE_EXISTED(10034, "电话号码重复"),
    CONTENT_NOT_NULL(10035, "内容不能为空"),
    NEED_USERNAME(10036, "姓名不能为空"),
    NEED_SCHOOLNAME(10037, "学校不能为空"),
    NEED_MAJORNAME(10038, "专业不能为空"),
    SCHOOL_DIFFERENT(10039, "学校不一致"),
    AGE_NOT_NULL(10040, "年龄不能为空"),
    HOMETOWN_NOT_NULL(10041, "籍贯不能为空"),
    IDCARDNUMBER_NOT_NULL(10042, "身份证号码不能为空"),
    IdCardFront_NOT_NULL(10043, "身份证正面照不能为空"),
    IdCardBack_NOT_NULL(10044, "身份证背面照不能为空"),
    Certificate_NOT_NULL(10045, "证明身份照不能为空"),
    KeyWord_NOT_NULL(10046, "搜索关键字为空"),
    NO_USERINFO(10047, "token中无用户信息"),
    STATUS_WRONG(10048,"状态异常"),
    ASSESS_REPEAT(10049,"不可重复评分"),






    //系统异常
    SYSTEM_ERROR(20000, "系统异常，请从控制台或日志中查看具体错误信息");

    /**
     * 异常码
     */
    Integer code;
    /**
     * 异常信息
     */
    String msg;

    XinYuanExceptionEnum(Integer code, String msg) {
        this.code = code;
        this.msg = msg;
    }

    public Integer getCode() {
        return code;
    }

    public void setCode(Integer code) {
        this.code = code;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }
}
