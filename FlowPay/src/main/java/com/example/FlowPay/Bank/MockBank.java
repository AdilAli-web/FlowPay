package com.example.FlowPay.Bank;

import java.time.Instant;

public class MockBank{
    public record Result(String status, Instant resolveAt){}


    public Result initiate(String scenario){
        return switch (scenario) {
            case "SUCCESS" -> new Result("SUCCESS", null);
            case "FAILED" -> new Result("FAILED", null);
            case "DELAYED_SUCCESS" -> new Result("PENDING", Instant.now().plusSeconds(8));
            default -> throw new IllegalArgumentException("unknow simulation scenario");
        };

    }


    public String check(Instant resolveAt){
        return resolveAt!=null && !Instant.now().isBefore(resolveAt)
                ? "SUCCESS" : "PENDING";
    }
}
