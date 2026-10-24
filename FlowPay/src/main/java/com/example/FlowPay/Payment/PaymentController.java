package com.example.FlowPay.Payment;

import com.example.FlowPay.merchant.MerchantService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/payments")
public class PaymentController {
    private final PaymentService paymentService;
    private final MerchantService merchantService;

    public PaymentController(PaymentService paymentService, MerchantService merchantService) {
        this.paymentService = paymentService;
        this.merchantService = merchantService;
    }

@PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public Payment createPayment(@RequestHeader("X-Api-Key") String key, @RequestHeader("idempotency-Key") String ik, @Valid @RequestBody PaymentDtos.Create create){

        Payment pay=paymentService.create(merchantService.authenticate(key).id,ik,create);
        return pay;
}

@GetMapping("/{id}")
    @ResponseStatus(HttpStatus.FOUND)
public Payment get(@RequestHeader("X-Api-Key") String key, @PathVariable String id)
{
    return paymentService.get(merchantService.authenticate(key).id,id);
}

@PostMapping("/{id}/process")
@ResponseStatus(HttpStatus.FOUND)
public Payment process( @RequestHeader("X-Api-Key") String key, @PathVariable String id)
{
    return paymentService.process(merchantService.authenticate(key).id,id);
}

    @PostMapping("/{id}/check")
    @ResponseStatus(HttpStatus.FOUND)
    public Payment check( @RequestHeader("X-Api-Key") String key, @PathVariable String id)
    {
        return paymentService.reconcile(merchantService.authenticate(key).id,id);
    }



}
