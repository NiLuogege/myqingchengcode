package com.qingcheng.controller.order;

import com.alibaba.dubbo.config.annotation.Reference;
import com.qingcheng.pojo.order.Order;
import com.qingcheng.service.order.OrderService;
import com.qingcheng.service.order.WeixinPayService;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.ServletInputStream;
import javax.servlet.http.HttpServletRequest;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.Map;

@RestController
public class WxPayController {

    @Reference
    OrderService orderService;
    @Reference
    private WeixinPayService weixinPayService;

    @GetMapping("/createNative")
    public Map createNative(String orderId) throws Exception {
        String userName = SecurityContextHolder.getContext().getAuthentication().getName();

        Order order = orderService.findById(orderId);

        if (order != null) {
            if ("0".equals(order.getPayStatus()) &&
                    "0".equals(order.getOrderStatus()) &&
                    userName.equals(order.getUsername())) {
                return weixinPayService.createNative(orderId, order.getPayMoney(), "http://qingcheng.easy.echosite.cn/wxpay/notify.do");
            }

        }
        return null;
    }


    /**
     * 回调
     */
    @RequestMapping("/notify")
    public void notifyLogic(HttpServletRequest request) throws Exception {
        System.out.println("支付成功回调。。。。");

        ServletInputStream inputStream = request.getInputStream();
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();

        byte[] buffer = new byte[1024];
        int len = 0;
        while ((len = inputStream.read(buffer)) != -1) {
            outputStream.write(buffer, 0, len);
        }

        outputStream.close();
        inputStream.close();

        String result = new String(outputStream.toByteArray(), "utf-8");
        weixinPayService.notifyLogic(result);


    }

    @GetMapping("/queryPayStatus")
    public Map queryPayStatus(String orderId){
        return  weixinPayService.queryPayStatus(orderId);
    }

}
