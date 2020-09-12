package com.app.base.data;

import com.fasterxml.jackson.databind.ObjectMapper;
import io.swagger.annotations.ApiModelProperty;

import java.io.IOException;
import java.io.StringWriter;

/**
 * restAPI服务返回结果集
 */
public class ApiResponseResult {

    //返回结果，成功为true，失败为false
    @ApiModelProperty(name = "result", value ="返回结果，成功为true，失败为false")
    protected boolean result;

    //返回信息
    @ApiModelProperty(name = "msg", value ="返回信息")
    protected String msg = "";

    //返回数据
    @ApiModelProperty(name = "data", value ="返回数据")
    protected Object data;

    //它自定义的状态码
    @ApiModelProperty(name = "status", value ="它自定义的状态码")
    protected String status;

    //返回数据数量
    @ApiModelProperty(name = "count", value ="返回数据数量")
    private int count;

    public static ApiResponseResult success() {
        return new ApiResponseResult(true);
    }

    public static ApiResponseResult failure() {
        return new ApiResponseResult(false);
    }

    public static ApiResponseResult success(String message) {
        return new ApiResponseResult(true, message);
    }

    public static ApiResponseResult failure(String message) {
        return new ApiResponseResult(false, message);
    }

    public ApiResponseResult(){
        super();
    }

    public ApiResponseResult(boolean success) {
        this(success, success ? "0" : "1");
    }

    public ApiResponseResult(boolean success, String message) {
        this(success, message, null);
    }

    public ApiResponseResult(boolean success, String message, Object data) {
        this.result = success;
        this.msg = message;
        this.data = data;
        this.status = success ? "0" : "1";
    }

    public ApiResponseResult(String status, String message, Object data) {
        this.msg = message;
        this.data = data;
        this.status = status;
    }

    public ApiResponseResult(String status, String message) {
        this.msg = message;
        this.status = status;
    }

    public String toJsonStr() {
        ObjectMapper objMapper = new ObjectMapper();
        StringWriter str = new StringWriter();
        try {
            objMapper.writeValue(str, this);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return str.toString();
    }

    public ApiResponseResult result(boolean result) {
        this.result = result;
        return this;
    }

    public ApiResponseResult message(String message) {
        this.msg = message;
        return this;
    }

    public ApiResponseResult data(Object data) {
        this.data = data;
        return this;
    }

    public ApiResponseResult status(String status) {
        this.status = status;
        return this;
    }

    public ApiResponseResult count(int count) {
        this.count = count;
        return this;
    }

    public boolean isResult() {
        return result;
    }

    public void setResult(boolean result) {
        this.result = result;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public void setMessage(String message){
        this.msg = message;
    }

    public Object getData() {
        return data;
    }

    public void setData(Object data) {
        this.data = data;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public int getCount() {
        return count;
    }

    public void setCount(int count) {
        this.count = count;
    }
}
