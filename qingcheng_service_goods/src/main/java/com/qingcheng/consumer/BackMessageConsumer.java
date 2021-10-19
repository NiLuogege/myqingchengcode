package com.qingcheng.consumer;

import com.alibaba.fastjson.JSON;
import com.qingcheng.pojo.order.OrderItem;
import com.qingcheng.service.goods.StockBackService;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.core.MessageListener;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;

public class BackMessageConsumer implements MessageListener {
    
    @Autowired
    StockBackService stockBackService;
    
    @Override
    public void onMessage(Message message) {
        System.out.println("收到消息了");

        try {
            String jsonString = new String(message.getBody());
            List<OrderItem> orderItems = JSON.parseArray(jsonString, OrderItem.class);
            stockBackService.addList(orderItems);
        } catch (Exception e) {
            e.printStackTrace();
            // TODO: 2021/10/19 记录日志
        }
    }
}
