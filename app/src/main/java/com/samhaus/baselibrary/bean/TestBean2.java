package com.samhaus.baselibrary.bean;

/**
 * Created by samhaus on 2017/9/12.
 */

public class TestBean2 {

    private int code;
    private String text;
    private boolean isRight;

    public boolean isRight() {
        return isRight;
    }

    public void setRight(boolean right) {
        isRight = right;
    }

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    @Override
    public String toString() {
        return "TestBean2{" +
                "code=" + code +
                ", text='" + text + '\'' +
                ", isRight=" + isRight +
                '}';
    }
}
