package com.verclamdistrib.woocomerce.models.api_response_model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * Created by muneeb.vectorcoder@gmail.com on 10-Jan-18.
 */

public class BaseResponse<T> {

    @SerializedName("status")
    @Expose
    private String status;
    @SerializedName("success")
    @Expose
    private String success;
    @SerializedName("message")
    @Expose
    private String message;
    @SerializedName("error")
    @Expose
    private String errorResponse;
    //private ErrorResponse errorResponse;
    @SerializedName("Data")
    @Expose
    private T data;
    
    
    public String getSuccess() {
        return success;
    }
    
    public void setSuccess(String success) {
        this.success = success;
    }
    
    public String getMessage() {
        return message;
    }
    
    public void setMessage(String message) {
        this.message = message;
    }
    
//    public ErrorResponse getErrorResponse() {
//        return errorResponse;
//    }
//
//    public void setErrorResponse(ErrorResponse errorResponse) {
//        this.errorResponse = errorResponse;
//    }


    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getErrorResponse() {
    return errorResponse;
}

    public void setErrorResponse(String errorResponse) {
        this.errorResponse = errorResponse;
    }
    public T getData() {
        return data;
    }
    
    public void setData(T data) {
        this.data = data;
    }
    
}

