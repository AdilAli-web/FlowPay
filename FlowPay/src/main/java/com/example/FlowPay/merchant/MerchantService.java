package com.example.FlowPay.merchant;

import com.example.FlowPay.Exception.ApiException;
import com.example.FlowPay.common.Ids;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.net.URI;
import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.HexFormat;
import java.util.Set;

@Service
public class MerchantService {
    private  final MerchantRepo merchantRepo;

private final Set<String> allowedHosts;
    public MerchantService(MerchantRepo merchantRepo, @Value("${app-webhook-hosts}") String  allowedHosts) {
        this.merchantRepo = merchantRepo;
        this.allowedHosts = Set.of(allowedHosts.split(","));
    }

    public static String hash(String value){

        try{
            return HexFormat.of()
                    .formatHex(MessageDigest.getInstance("SHA-256").digest(value.getBytes(StandardCharsets.UTF_8)));
        } catch (NoSuchAlgorithmException e) {
            throw new IllegalArgumentException(e);
        }
    }

    @Transactional
    public MerchantDto.Registered register(MerchantDto.Register registering){
        Merchant m=new Merchant();
m.id= Ids.next("mer");
m.setName(registering.name());
m.setEmail(registering.email());
//key
        String key=Ids.next("fp_text");
        m.setApiKeyHash(hash(key));
        m.setWebhookSecret(Ids.next("whsec"));
merchantRepo.save(m);

return new MerchantDto.Registered(m,m.getApiKeyHash(),m.getWebhookSecret());


    }



    public Merchant authenticate(String key){
        if(key==null || key.isBlank()){
            throw new ApiException(HttpStatus.UNAUTHORIZED,"A valid x-Api-key is required");
        }
        return merchantRepo.findByApiKeyHash(hash(key)).
                orElseThrow(()->new ApiException(HttpStatus.UNAUTHORIZED,"Invalid API KEY"));
    }



public Merchant LockMerchant(String key){
return merchantRepo.lockById(key).orElseThrow(ApiException::missing);

}



    public void validate(String value) {
        if (value == null || value.isBlank()) return;
        try {
            java.net.URI u = URI.create(value);
            if (!Set.of("http", "https").contains(u.getScheme()) || !allowedHosts.contains(u.getHost())
                    || u.getUserInfo() != null || u.getFragment() != null) {
                throw new IllegalArgumentException();
            }
        } catch (Exception e) {
            throw ApiException.bad("webhook url must be HTTP(s) and host in webhook_hosts");
        }
    }



@Transactional()
public Merchant update(String id,MerchantDto.Update update){

Merchant merchant=LockMerchant(id);
validate(update.webhookUrl());
    merchant.setName(update.name());
    merchant.setEmail(update.email());
    merchant.setWebhookUrl(update.webhookUrl());
return merchant;

}

}



