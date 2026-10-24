package com.example.FlowPay.transaction;

import com.example.FlowPay.Payment.Payment;
import com.example.FlowPay.Payment.PaymentService;
import com.example.FlowPay.merchant.MerchantService;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;

@RestController
@RequestMapping("/api/transactions")
public class TransactionController {
    private final TransactionsService transactionsService;
    private final MerchantService merchantService;
    private final PaymentService paymentService;

    public TransactionController(TransactionsService transactionsService, MerchantService merchantService, PaymentService paymentService) {
        this.transactionsService = transactionsService;
        this.merchantService = merchantService;
        this.paymentService = paymentService;
    }

    @GetMapping
    public List<Payment> list(@RequestHeader("X-Api-Key") String key,
                              @RequestParam(required = false) String status,
                              @RequestParam(required = false) LocalDate from, @RequestParam(required = false) LocalDate to)
    {

return transactionsService.list(key,status,from,to);

    }



    public List<Leger> ledger(@RequestHeader("X-Apu-Key") String key){
        return transactionsService.ledger(merchantService.authenticate(key).id);
    }

    @GetMapping("/{id}")
    public Payment paymentDetails(@RequestHeader("X-Apu-Key") String key,@PathVariable String id){
return paymentService.get(merchantService.authenticate(key).id, id);
    }




}
