package com.example.FlowPay.Payment;

import com.example.FlowPay.Bank.MockBank;
import com.example.FlowPay.Exception.ApiException;
import com.example.FlowPay.merchant.MerchantService;
import com.example.FlowPay.common.Idempotency;
import com.example.FlowPay.common.Ids;
import com.example.FlowPay.merchant.Merchant;
import com.example.FlowPay.order.OrderService;
import com.example.FlowPay.order.PaymentOrder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.List;


@Service
public class PaymentService {
    private final PaymentRepo repo;
    private  final MerchantService merchantService;
    private final OrderService orders;
    private final MockBank mockBank;

    public PaymentService(PaymentRepo repo, MerchantService merchantService, OrderService orders, MockBank mockBank) {
        this.repo = repo;
        this.merchantService = merchantService;
        this.orders = orders;
        this.mockBank = mockBank;
    }

public static long fee(long amount)
{
//    2--->percent fees
    return (Math.multiplyExact(amount,2)+99)/100;
}

@Transactional
public Payment create(String m,String key,PaymentDtos.Create request){
    Idempotency.validate(key);
    merchantService.LockMerchant(m);
Payment existing = repo.findByMerchantIdAndIdempotencyKey(m, key).orElse(null);
if(existing!=null)
{

    if(!existing.orderId.equals(request.orderId())  ||  !existing.method.equals(request.method().name())){
        throw ApiException.conflict("idempotency key reused with different request");
    }
    return existing;

}
PaymentOrder order=new PaymentOrder();
if(order.status.equals("PAID") || repo.existsByOrderIdAndStatusIn(order.id, List.of("CREATED","PENDING","SUCCESS"))){
    throw ApiException.conflict("order Already paid or has an active payment");
}

Payment payment=new Payment();
    payment.id= Ids.next("pay");
    payment.merchantId=m;
    payment.orderId=order.id;
    payment.amount=order.amount;
    payment.idempotencyKey=key;
    payment.method=request.scenario().name();
    return repo.save(payment);
}




public Payment get(String m,String id){
        Payment p=repo.findById(id).orElseThrow(ApiException::missing);
        if(!p.merchantId.equals(m)) throw ApiException.missing();
        return p;
}


@Transactional
    public Payment process(String m,String id){
    Merchant merchant=merchantService.LockMerchant(m);
    Payment p=get(m,id);
    if(!p.status.equals("CREATED")) return p;
    MockBank.Result result=mockBank.initiate(p.scenario);
    p.resolveAt=result.resolveAt();
finish(merchant,p,result.status());
return p;
}


private void finish(Merchant m,Payment p, String Status){
        p.status=Status;
        if(Status.equals("SUCCESS")){
            p.fee=fee(p.amount);
            orders.get(m.id,p.orderId).status="PAID";
        }
        else if(Status.equals("FAILED")){

        }
}


@Transactional
public Payment reconcile(String m,String id){
        Merchant merchant=merchantService.LockMerchant(m);
        Payment p=get(m,id);
        if(p.status.equals("PENDING")){
            finish(merchant,p,"PENDING");
        }
        return p;
}


public List<Payment> list(String m){
        return repo.findByMerchantIdOrderByCreatedAtDesc(m);
}

}
