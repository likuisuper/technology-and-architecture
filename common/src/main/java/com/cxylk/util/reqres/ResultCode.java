package com.cxylk.util.reqres;

/**
 * 响应码枚举，参考HTTP状态码的语义
 *
 * @author admin
 */
public enum ResultCode {
    //成功
    SUCCESS(200),
    //失败
    FAIL(400),
    //未认证（签名错误）
    UNAUTHORIZED(401),
    // 页面未找到
    PAGE_NOT_FOUND(404),
    //服务器内部错误
    INTERNAL_SERVER_ERROR(500),
    //参数校验不通过 自己定义的errodcode
    BASE_PARAM_ERR_CODE(461),
    //参数校验不通过 自己定义的errodcode
    BASE_BAD_REQUEST_ERR_CODE(462),

    // 自定义错误类型从10000开始设置
    /**
     *
     */
    PHONE_NOT_VALID(10000, "手机号码不正确"),
    /**
     *
     */
    TASK_STATUS_ERROR(1001,"状态错误"),
    // 自定义错误结束
    ACCOUNT_IS_REFUSE(210);

    /**
     * 代码编号
     */
    private final int code;

    /**
     * 默认消息
     */
    private final String message;

    ResultCode(int code) {
        this.code = code;
        this.message = null;
    }

    ResultCode(int code, String message) {
        this.code = code;
        this.message = message;
    }

    public int code() {
        return code;
    }

    /**
     * @return
     */
    public int getCode() {
        return code;
    }

    /**
     * @return
     */
    public String getMessage() {
        return message;
    }
}
