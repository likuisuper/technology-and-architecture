package com.cxylk.util.reqres;

/**
 * @author admin
 */
public class ResultGenerator {

    /**
     *
     */
    private static final String DEFAULT_SUCCESS_MESSAGE = "SUCCESS";
    /**
     *
     */
    private static final String DEFAULT_FAIL_MESSAGE = "ERROR";

    private ResultGenerator() {
        throw new IllegalStateException("Utility class");
    }

    public static <T> Result<T> success() {
        return new Result<T>()
                .setCode(ResultCode.SUCCESS)
                .setMessage(DEFAULT_SUCCESS_MESSAGE);
    }

    public static <T> Result<T> success(ResultCode resultCode, T data) {
        return new Result()
                .setCode(resultCode)
                .setData(data);
    }

    public static <T> Result<T> success(T data) {
        return new Result<T>()
                .setCode(ResultCode.SUCCESS)
                .setMessage(DEFAULT_SUCCESS_MESSAGE)
                .setData(data);
    }

    public static <T> Result<T> error(String message) {
        return new Result<T>()
                .setCode(ResultCode.FAIL)
                .setMessage(message);
    }

    public static <T> Result<T> error() {
        return new Result()
                .setCode(ResultCode.FAIL)
                .setMessage(DEFAULT_FAIL_MESSAGE);
    }

    /**
     * 根据resultCode产生一个对象
     *
     * @param resultCode
     * @param <T>
     * @return
     */
    public static <T> Result<T> gen(ResultCode resultCode) {
        return new Result()
                .setCode(resultCode.getCode())
                .setMessage(resultCode.getMessage());
    }

    public static <T> Result<T> error(ResultCode resultCode, String message) {
        return new Result<T>()
                .setCode(resultCode)
                .setMessage(message);
    }

    public static <T> Result<T> error(Integer code, String message) {
        return new Result<T>()
                .setCode(code)
                .setMessage(message);
    }

    public static <T> Result<T> error(Integer resultCode,String message, T data) {
        return new Result()
                .setCode(resultCode)
                .setMessage(message)
                .setData(data);
    }
}
