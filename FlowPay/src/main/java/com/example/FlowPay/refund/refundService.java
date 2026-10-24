package com.example.FlowPay.refund;

import com.example.FlowPay.Exception.ApiException;
import com.example.FlowPay.Kafka.EventService;
import com.example.FlowPay.Payment.Payment;
import com.example.FlowPay.Payment.PaymentService;
import com.example.FlowPay.common.Idempotency;
import com.example.FlowPay.common.Ids;
import com.example.FlowPay.merchant.Merchant;
import com.example.FlowPay.merchant.MerchantService;
import com.example.FlowPay.transaction.LegerService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class refundService {
    private final RefundRepo repo;
    private final MerchantService merchantService;
    private final PaymentService paymentService;
    private final LegerService legerService;
    private final EventService eventService;

    public refundService(RefundRepo repo, MerchantService merchantService, PaymentService paymentService, LegerService legerService, EventService eventService) {
        this.repo = repo;
        this.merchantService = merchantService;
        this.paymentService = paymentService;
        this.legerService = legerService;
        this.eventService = eventService;
    }


@Transactional
    public Refund create(String m,String key,RefundDtos.create create){

    Idempotency.validate(key);
    Merchant merchant= merchantService.LockMerchant(m);

    Refund old=repo.findByMerchantIdAndIdempotencyKey(m,key).orElse(null);
    if(old!=null){
        if(!old.paymentId.equals(create.paymentId()) || old.amount != create.amount())
        {
throw ApiException.conflict("Idempotency key reused with different refund");
        }
        return old;

    }
    Payment p=paymentService.get(m,create.paymentId());
    if(!p.status.equals("SUCCESS")){
        throw ApiException.conflict("Only success payment can be refunded");
    }
    if(create.amount()<1 || create.amount()>p.amount-p.refunded){
        throw ApiException.conflict("Refund exceeds remaining payment amount");
    }

    long before=p.refunded;
    long after=before+create.amount();
    long feeReturn=(p.fee * after/p.amount - (p.fee*before/p.amount));

    long debit=create.amount()-feeReturn;
    if(merchant.balance<debit){
        throw ApiException.conflict("Insufficient available merchant balance");
    }

    Refund r=new Refund();
    r.id= Ids.next("rfnd");
    r.merchantId=m;
    r.idempotencyKey=key;
    r.paymentId=create.paymentId();
    r.amount=create.amount();

    p.refunded=after;
    repo.save(r);

    legerService.record(merchant,r.id,"REFUND",-debit);

//    eventService.create
return r;

}



public List<Refund> list(String s){
        return repo.findByMerchantIdOrderByCreatedAtDesc(s);
}

}
