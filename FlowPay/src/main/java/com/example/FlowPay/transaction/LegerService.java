package com.example.FlowPay.transaction;

import com.example.FlowPay.common.Ids;
import com.example.FlowPay.merchant.Merchant;
import com.example.FlowPay.merchant.MerchantService;
import org.springframework.stereotype.Service;

@Service
public class LegerService {
    public LegerService(LedgerEntryRepo repo) {
        this.repo = repo;
    }

    private final LedgerEntryRepo repo;


    public void record(Merchant m,String ref,String type,long delta){
        m.balance=Math.addExact(m.balance, delta);
        Leger leger=new Leger();
        leger.id= Ids.next("txn");
        leger.merchantId=m.id;
        leger.referenceId=ref;
        leger.type=type;
        leger.amount=delta;
        leger.balanceAfter=m.balance;
        repo.save(leger);
    }


}
