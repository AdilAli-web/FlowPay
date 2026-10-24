package com.example.FlowPay.Bank;

import com.example.FlowPay.Payment.PaymentRepo;
import com.example.FlowPay.Payment.PaymentService;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Component
@ConditionalOnProperty(name="app.jobs-enabled",havingValue = "true")
public class Bank {

    private final PaymentRepo paymentRepo;
    private final PaymentService paymentService;

    public Bank(PaymentRepo paymentRepo, PaymentService paymentService) {
        this.paymentRepo = paymentRepo;
        this.paymentService = paymentService;
    }


@Scheduled(fixedDelay = 2000)
public void completeDelayed(){

}
}
