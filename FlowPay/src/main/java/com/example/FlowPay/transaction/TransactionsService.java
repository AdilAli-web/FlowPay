package com.example.FlowPay.transaction;

import com.example.FlowPay.Exception.ApiException;
import com.example.FlowPay.Payment.Payment;
import com.example.FlowPay.Payment.PaymentService;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.ZoneOffset;
import java.util.List;

@Service
public class TransactionsService {
    private final PaymentService paymentService;
    private final LedgerEntryRepo repo;

    public TransactionsService(PaymentService paymentService, LedgerEntryRepo repo) {
        this.paymentService = paymentService;
        this.repo = repo;
    }


    public List<Payment> list(String merchant, String status, LocalDate from, LocalDate to){

        if(from!=null && to!=null && from.isAfter(to)){
         throw ApiException.bad("From data must not be after to date");
        }
return paymentService.list(merchant).stream()
        .filter(p->status==null || status.isBlank() || status.equals(p.status))
        .filter(p->
                from==null || p.createdAt.isBefore(from.atStartOfDay(ZoneOffset.UTC).toInstant()))
        .filter(p-> to==null ||  p.createdAt.isBefore(to.plusDays(1).atStartOfDay(ZoneOffset.UTC).toInstant()))
        .toList();
    }


    public List<Leger> ledger(String m){
        return repo.findByMerchantIdOrderByCreatedAtDesc(m);
    }

}
