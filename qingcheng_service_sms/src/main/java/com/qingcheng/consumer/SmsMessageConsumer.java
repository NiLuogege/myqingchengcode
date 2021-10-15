package com.qingcheng.consumer;

import com.alibaba.fastjson.JSON;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.core.MessageListener;

import java.util.Map;

public class SmsMessageConsumer implements MessageListener {
    @Override
    public void onMessage(Message message) {
        String jsonString = new String(message.getBody());
        Map<String,String> map = JSON.parseObject(jsonString, Map.class);

        String phone = map.get("phone");//手机号
        String code = map.get("code");//验证码
        System.out.println("手机号："+phone+" 验证码："+code);
    }

}
