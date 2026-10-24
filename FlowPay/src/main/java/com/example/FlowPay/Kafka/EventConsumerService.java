package com.example.FlowPay.Kafka;
import com.example.FlowPay.Exception.ApiException;
import com.example.FlowPay.Webhook.WebHook;
import com.example.FlowPay.Webhook.WebHookRepo;
import com.example.FlowPay.common.Ids;
import com.example.FlowPay.merchant.Merchant;
import com.example.FlowPay.merchant.MerchantRepo;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Instant;

@Service
public class EventConsumerService {
    private final EventRepo events;
   private final WebHookRepo webhooks;
    private final MerchantRepo merchants;

    public EventConsumerService(EventRepo repo, WebHookRepo webhooks, MerchantRepo merchants) {
        this.events = repo;
        this.webhooks = webhooks;
        this.merchants = merchants;
    }

    @Transactional
    public void consume(String id)
    {
        Event e=events.lockById(id).orElseThrow();
        if(e.consumedAt!=null) return;
        Merchant m=merchants.findById(e.merchantId).orElseThrow(ApiException::missing);
        WebHook w=new WebHook();
        w.id= Ids.next("wh");
        w.eventId=e.id;
        w.merchantId=m.id;
        w.url=m.webhookUrl;
        if (w.url == null || w.url.isBlank())
        {
            w.status="SKIPPED";
            w.lastError="No Webhook URL configured";
        }
        webhooks.save(w);
        e.consumedAt= Instant.now();
    }
}
