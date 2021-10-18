package com.qingcheng.controller.order;

import com.alibaba.dubbo.config.annotation.Reference;
import com.alibaba.dubbo.config.annotation.Service;
import com.qingcheng.entity.Result;
import com.qingcheng.service.order.CartService;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("card")
public class CardController {

    @Reference
    private CartService cartService;

    @GetMapping("findCartList")
    public Result findCartList() {
        String username = SecurityContextHolder.getContext().getAuthentication().getName();
        List<Map<String, Object>> cartList = cartService.findCartList(username);
        return new Result(cartList);
    }

    @GetMapping("addItem")
    public Result addItem(String skuId, int num) {
        System.out.println("addItem skuId="+skuId+" num="+num);
        String username = SecurityContextHolder.getContext().getAuthentication().getName();
        cartService.addItem(username, skuId, num);
        return new Result();
    }

    @GetMapping("updateChecked")
    public Result updateChecked(String skuId, boolean check) {
        String username = SecurityContextHolder.getContext().getAuthentication().getName();
        cartService.updateCheck(username, skuId, check);
        return new Result();
    }
}
