package com.example.FlowPay.merchant;

import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/merchants")
public class MerchantController {
    private final MerchantService merchantService;

    public MerchantController(MerchantService merchantService) {
        this.merchantService = merchantService;
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
public MerchantDto.Registered register( @Valid @RequestBody MerchantDto.Register register){
        return merchantService.register(register);
}

@GetMapping("/me")
public Merchant me (@RequestHeader("X-Api-Key") String key){
return merchantService.authenticate(key);
}

@PutMapping("/me")
public Merchant update(@RequestHeader("X-Api-Key") String key,@Valid  @RequestBody MerchantDto.Update update){
        return merchantService.update(merchantService.authenticate(key).id,update);
}





}
