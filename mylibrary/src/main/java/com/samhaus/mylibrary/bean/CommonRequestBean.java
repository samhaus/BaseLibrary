package com.samhaus.mylibrary.bean;



/**
 * Created by samhaus on 2017/9/12.
 */

public class CommonRequestBean<E> {

    private String error_code;
    private String reason;
    private E result;

    public CommonRequestBean(String error_code, String reason, E result) {
        this.error_code = error_code;
        this.reason = reason;
        this.result = result;
    }

    public CommonRequestBean() {
    }

    public String getError_code() {
        return error_code;
    }

    public void setError_code(String error_code) {
        this.error_code = error_code;
    }

    public String getReason() {
        return reason;
    }

    public void setReason(String reason) {
        this.reason = reason;
    }

    public E getResult() {
        return result;
    }

    public void setResult(E result) {
        this.result = result;
    }

    @Override
    public String toString() {
        return "CommonRequestBean{" +
                "error_code=" + error_code +
                ", reason='" + reason + '\'' +
                ", result=" + result +
                '}';
    }
}
