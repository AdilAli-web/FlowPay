package com.example.FlowPay.common;

import com.example.FlowPay.Exception.ApiException;

public class Idempotency {

    private Idempotency(){

    }

    public static void validate(String key){
        if(key==null ||!key.matches("[A-za-z0-9_-]{8,100}")){
            throw ApiException.bad("Idempotency key must contain 8-100 letters, digits,underscores or hyphens");
        }
    }
}
