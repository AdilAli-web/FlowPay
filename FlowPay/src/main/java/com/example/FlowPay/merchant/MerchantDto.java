package com.example.FlowPay.merchant;


import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public class MerchantDto {

    public record Register(@NotBlank @Size(max=100) String name, @Email @NotBlank @Size(max=100) String email)
    {};

    public record Update(@NotBlank @Size(max=100) String name, @Email @NotBlank @Size(max=100) String email
    ,
                         String webhookUrl
    )
    {};

    public record Registered(Merchant merchant,String apiKey, String webhookSecret)
    {};
}
