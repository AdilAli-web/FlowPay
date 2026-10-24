package com.example.FlowPay.Settlement;

import com.example.FlowPay.Exception.ApiException;
import com.example.FlowPay.common.Idempotency;
import com.example.FlowPay.common.Ids;
import com.example.FlowPay.merchant.Merchant;
import com.example.FlowPay.merchant.MerchantService;
import com.example.FlowPay.transaction.LegerService;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class SettlementService {
    private final SettleRepo repo;
    private final MerchantService merchantService;
    private final LegerService ledger;

    public SettlementService(SettleRepo repo, MerchantService merchantService, LegerService ledger) {
        this.repo = repo;
        this.merchantService = merchantService;
        this.ledger = ledger;
    }



    public Settlement create(String m,String key)
    {
        Idempotency.validate(key);
        Merchant merchant=merchantService.LockMerchant(m);
        Settlement old=repo.findByMerchantIdAndIdempotencyKey(m,key).orElse(null);
        if(old!=null) return old;
        if(merchant.balance<0) throw ApiException.conflict("No available balance to settle");
        Settlement s= new Settlement();
        s.id= Ids.next("stl");
        s.merchantId=m;
        s.idempotencyKey=key;
        s.amount=merchant.balance;
        repo.save(s);
        ledger.record(merchant,s.id,"SETTLEMENT",-s.amount);
        return s;
    }

    public List<Settlement> list(String  s)
    {
        return repo.findByMerchantIdOrderByCreatedAtDesc(s);
    }
}
