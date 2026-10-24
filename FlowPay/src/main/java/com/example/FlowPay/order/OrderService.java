package com.example.FlowPay.order;

import com.example.FlowPay.Exception.ApiException;
import com.example.FlowPay.merchant.MerchantService;
import com.example.FlowPay.common.Ids;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class OrderService {
 private final PaymentOrderRepo repo;

    private final MerchantService merchantService;
    public OrderService(PaymentOrderRepo repo, MerchantService merchantService) {
        this.repo = repo;
        this.merchantService = merchantService;
    }



@Transactional
    public PaymentOrder create(String merchantId,OrderDtos.create r){
        merchantService.LockMerchant(merchantId);
PaymentOrder paymentOrder=new PaymentOrder();

paymentOrder.id= Ids.next("order");
paymentOrder.merchantId=merchantId;
paymentOrder.amount=r.amount();
paymentOrder.receipt=r.receipt();
return repo.save(paymentOrder);
    }


    public PaymentOrder get(String merchantId, String id){

        PaymentOrder order=repo.findById(id).orElseThrow(ApiException::missing);
        if(!order.merchantId.equals(merchantId)) throw ApiException.missing();
        return order;

    }


    public List<PaymentOrder> list(String merchantId){
       List<PaymentOrder> all= repo.findByMerchantIdOrderByCreatedAtDesc(merchantId);
       return all;

    }




}
