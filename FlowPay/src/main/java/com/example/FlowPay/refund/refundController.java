package com.example.FlowPay.refund;

import com.example.FlowPay.Payment.Payment;
import com.example.FlowPay.Payment.PaymentDtos;
import com.example.FlowPay.merchant.MerchantService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/refund")
public class refundController {
    private final refundService service;
private final MerchantService merchantService;

    public refundController(refundService service, MerchantService merchantService) {
        this.service = service;
        this.merchantService = merchantService;
    }




    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public Refund createPayment(@RequestHeader("X-Api-Key") String key, @RequestHeader("idempotency-Key") String ik, @Valid @RequestBody RefundDtos.create create){

      Refund refund=service.create(merchantService.authenticate(key).id,ik,create);
        return refund;
    }



    public List<Refund> list(@RequestHeader("X-Api-Key") String key){
        return service.list(merchantService.authenticate(key).id);
    }
}
