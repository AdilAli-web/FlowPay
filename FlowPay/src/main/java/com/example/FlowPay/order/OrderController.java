package com.example.FlowPay.order;

import com.example.FlowPay.merchant.MerchantService;
import jakarta.validation.Valid;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/orders")
public class OrderController {


//    POST /api/orders — create an order.
//GET /api/orders/{id} — retrieve one order.
    //GET /api/orders — retrieve orders belonging to the merchant.
//    ---------------------------------------------------------------
//OrderService handles order operations.
//    MerchantService verifies the API key and identifies the merchant.
private final OrderService orderService;
private final MerchantService merchantService;

    public OrderController(OrderService orderService, MerchantService merchantService) {
        this.orderService = orderService;
        this.merchantService = merchantService;
    }

    @PostMapping
    public PaymentOrder createOrder(   @RequestHeader("X-Api-Key") String key,@Valid @RequestBody OrderDtos.create create){
        PaymentOrder creation=orderService.create(merchantService.authenticate(key).id,create);
        return creation;
    }

    @GetMapping("/{id}")
    public PaymentOrder getOrder(@RequestHeader("X-Api-Key") String key,@PathVariable String id){
        return orderService.get(merchantService.authenticate(key).id,id);
    }

    @GetMapping
    public List<PaymentOrder> getAllOrders(@RequestHeader("X-Api-Key") String key){
        return orderService.list(merchantService.authenticate(key).id);
    }






}
