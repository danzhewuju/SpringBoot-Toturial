package com.satan.common;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;

@Data
public class Result<T> implements Serializable {

    @ApiModelProperty("状态码，0表示正常，非0表示异常")
    private int code;
    @ApiModelProperty("响应信息，通常为异常信息")
    private String message;
    @ApiModelProperty("返回数据")
    private T data;

    public static <T> Result succ(T data) {
        return succ(0, "操作成功", data);
    }

    public static Result succ(int code, String msg, Object data) {
        Result r = new Result();
        r.setCode(code);
        r.setMessage(msg);
        r.setData(data);
        return r;
    }

    public static Result succ( String msg, Object data) {
        Result r = new Result();
        r.setMessage(msg);
        r.setData(data);
        return r;
    }

    public static Result fail(String msg) {
        return fail(-400, msg, null);
    }

    public static Result fail(String msg, Object data) {
        return fail(-400, msg, data);
    }

    public static Result fail(int code, String msg, Object data) {
        Result r = new Result();
        r.setCode(code);
        r.setMessage(msg);
        r.setData(data);
        return r;
    }

    public static Result unLogin() {
        return Result.fail(403, "un-login", "http://dashboard-mng.bilibili.co/loginPage");
    }

    public static <T>  Result<T> failAtOpenApi() {
        return Result.fail(-1001, "token is invalid", null);
    }

    public static <T>  Result<T> notDataStaff() {
        return Result.fail(-1002, "not belong to data center staff", null);
    }
}
