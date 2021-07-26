package com.cxylk.util.reqres;


import com.cxylk.util.JsonUtils;
import com.cxylk.util.TraceUtils;

/**
 * 统一API响应结果封装
 *
 * @author admin
 */
public class Result<T> {
    /**
     * 响应编号，参考ResultCode
     */
    private int code;
    /**
     * 消息
     */
    private String message;
    /**
     * 请求流水号
     */
    private String traceLogId = TraceUtils.getCurrentTrace();
    /**
     * 数据
     */
    private T data;

    public Result setCode(ResultCode resultCode) {
        this.code = resultCode.code();
        return this;
    }

    public Result setCode(int resultCode) {
        this.code = resultCode;
        return this;
    }

    public int getCode() {
        return code;
    }

    public String getMessage() {
        return message;
    }

    public Result setMessage(String message) {
        this.message = message;
        return this;
    }

    public String getTraceLogId() {
        return traceLogId;
    }

    public void setTraceLogId(String traceLogId) {
        this.traceLogId = traceLogId;
    }

    public T getData() {
        return data;
    }

    public Result setData(T data) {
        this.data = data;
        return this;
    }

    @Override
    public String toString() {
        return JsonUtils.toJson(this);
    }

    public Boolean isSuccess() {
        return this.code - ResultCode.SUCCESS.code() == 0;
    }
}
