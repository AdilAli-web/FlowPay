package com.example.FlowPay.Payment;

import jakarta.validation.constraints.NotBlank;

public class PaymentDtos {
    public enum Method{
        UPI,
        CARD,
        WALLET,
        NETBANKING
    }


    public enum Scenario{
        SUCCESS,
        FAILED,
        TIMEOUT,
        DELAYED_SUCCESS
    }


    public record Create(@NotBlank String orderId,@NotBlank Method method,@NotBlank Scenario scenario)  {}
}
