package com.example.FlowPay.Exception;

import org.springframework.http.HttpStatus;

public class ApiException extends RuntimeException{

    public ApiException(HttpStatus status,String s) {
//        passes the message to the parent RuntimeException class. That's why you can later retrieve it with:
        super(s);
        this.status = status;
    }

    public final HttpStatus status;

    public static ApiException  bad(String s){
        return new ApiException(HttpStatus.BAD_REQUEST,s);
    }

    public static ApiException  conflict(String s){

        return new ApiException(HttpStatus.CONFLICT,s);
    }
    public static ApiException  missing(){

        return new ApiException(HttpStatus.NOT_FOUND,"Resource not found ");
    }

}
