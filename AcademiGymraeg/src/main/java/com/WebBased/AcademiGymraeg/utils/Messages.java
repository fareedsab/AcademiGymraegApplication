package com.WebBased.AcademiGymraeg.utils;

public class Messages<T> {

    private int status;
    private String message;
    private String code;

    private T data;
    private T body;


    public Messages<T> setStatus(int status)
    {
        this.status = status;
        return this;
    }

    public Messages<T> setMessage(String message)
    {
        this.message = message;
        return this;
    }

    public Messages<T> setData(T data)
    {
        this.data = data;
        return this;
    }

    public Messages<T> setCode(String code)
    {
        this.code = code;
        return this;
    }

    public int getStatus() {
        return status;
    }

    public String getMessage() {
        return message;
    }

    public String getCode() {
        return code;
    }

    public T getData() {
        return data;
    }

    public T getBody() {
        return body;
    }

    public void setBody(T body) {
        this.body = body;
    }
}