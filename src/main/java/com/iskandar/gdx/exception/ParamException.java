package com.iskandar.gdx.exception;

/**
 * Created by xlf on 2018/8/20.
 */
public class ParamException extends RuntimeException {
    private Integer code = 300;
    private String msg = "参数异常";

    public ParamException() {
    }

    public ParamException(Integer code, String msg) {
        super(msg);
        this.code = code;
        this.msg = msg;
    }

    public ParamException(Integer code) {
        super("参数异常");
        this.code = code;
    }

    public ParamException(String msg) {
        super(msg);
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
