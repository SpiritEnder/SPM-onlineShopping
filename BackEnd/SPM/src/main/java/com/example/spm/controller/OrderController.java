package com.example.spm.controller;

import com.example.spm.mapper.OrderMapper;
import com.example.spm.pojo.Order;
import com.example.spm.service.OrderService;
import com.example.spm.pojo.OrderItem;
import com.example.spm.pojo.OrderRequest;
import com.example.spm.pojo.Result;
import com.example.spm.utils.JwtUtil;
import com.example.spm.utils.ThreadLocalUtil;
import org.apache.ibatis.annotations.Param;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;


@RestController
@RequestMapping("/order")
public class OrderController {

    @Autowired
    private OrderService orderservice;


    @PostMapping("")
    public Result submitOrder(@RequestHeader("Authorization") String authorization,
                              @RequestBody OrderRequest orderRequest) {
        // 获取解析后的参数
//        Long addressId = orderRequest.getAddressId();
//        String paymentMethod = orderRequest.getPaymentMethod();
//        List<OrderItem> items = orderRequest.getItems();

        Map<String, Object> userInfo = JwtUtil.parseToken(authorization);

        orderservice.submitOrder(userInfo, orderRequest);

        return Result.success();
    }


    @GetMapping("/list")
    public Result ListOrders(
            @RequestParam(required = false) String status){
        Map<String, Object> map = ThreadLocalUtil.get();
        Integer userId = (Integer) map.get("id");
        List<Order> byUserIdAndStatus = orderservice.findByUserId(userId, status);
        return Result.success(byUserIdAndStatus);
    }

    /*
    * 对于status这个属性，0 未支付，1 已支付，2 待发货，3 运输中，4 已完成 ，5 已取消
    * 该接口是将订单的状态修改为代发货*/
    @PutMapping("/update")
    public Result updateOrder(@Param("orderId") Integer orderId, @Param("productId") Integer productId){
        orderservice.updateOrderLogistics(orderId, productId);
        return Result.success();
    }

    @PutMapping("/send")
    public Result sendOrder(@Param("orderId") Integer orderId, @Param("trackingNumber") String trackingNumber){
        orderservice.sendOrder(orderId, trackingNumber);
        return Result.success();
    }

     @PutMapping("/confirm")
    public Result confirmOrder(Integer orderId){
        orderservice.confirmOrder(orderId);
        return Result.success();
    }

    @PutMapping("/confirm/cancel")
    public Result cancelOrder(Integer orderId){
        orderservice.cancelOrder(orderId);
        return Result.success();
    }

    @GetMapping("/detail")
    public Result getDetail(Integer orderId){
        Order order = orderservice.getDetail(orderId);
        return Result.success(order);
    }

    @GetMapping("/logistics")
    public Result getLogistics( @RequestParam(required = false) Integer orderId,
                                @RequestParam(required = false) Integer productId){
        return Result.success(orderservice.getLogistics(orderId, productId));
    }

    @GetMapping("/checkout")
    public Result checkout(Integer orderId){
        BigDecimal totalAmount = orderservice.getDetail(orderId).getTotalAmount();
        return Result.success("Total:" +  totalAmount);
    }
}

