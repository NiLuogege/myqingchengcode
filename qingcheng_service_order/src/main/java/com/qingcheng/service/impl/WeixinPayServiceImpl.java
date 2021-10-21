package com.qingcheng.service.impl;

import com.alibaba.dubbo.config.annotation.Reference;
import com.alibaba.dubbo.config.annotation.Service;
import com.github.wxpay.sdk.Config;
import com.github.wxpay.sdk.WXPayRequest;
import com.github.wxpay.sdk.WXPayUtil;
import com.qingcheng.service.order.OrderService;
import com.qingcheng.service.order.WeixinPayService;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.HashMap;
import java.util.Map;

@Service
public class WeixinPayServiceImpl implements WeixinPayService {

    @Autowired
    Config config;

    @Autowired
    OrderService orderService;

    @Autowired
    RabbitTemplate rabbitTemplate;

    @Override
    public Map createNative(String orderId, Integer money, String notifyUrl) throws Exception {

        //1.创建参数
        Map<String, String> param = new HashMap();//创建参数
        param.put("appid", config.getAppID());//公众号
        param.put("mch_id", config.getMchID());//商户号
        param.put("nonce_str", WXPayUtil.generateNonceStr());//随机字符串
        param.put("body", "青橙");//商品描述
        param.put("out_trade_no", orderId);//商户订单号
        param.put("total_fee", money + "");//总金额（分）
        param.put("spbill_create_ip", "127.0.0.1");//IP
        param.put("notify_url", notifyUrl);//暂时随便写一个
        param.put("trade_type", "NATIVE");//交易类型

        String signedXml = WXPayUtil.generateSignedXml(param, config.getKey());

        WXPayRequest wxPayRequest = new WXPayRequest(config);
        String xmlRequest = wxPayRequest.requestWithCert("/pay/unifiedorder", null, signedXml, false);


        Map<String, String> resultMap = WXPayUtil.xmlToMap(xmlRequest);


        Map m = new HashMap();
        m.put("code_url", resultMap.get("code_url"));
        m.put("total_fee", money + "");
        m.put("out_trade_no", orderId);
        return m;
    }

    @Override
    public void notifyLogic(String xml) throws Exception {
        //xml to map
        Map<String, String> map = WXPayUtil.xmlToMap(xml);

        //验证签名
        boolean signatureValid = WXPayUtil.isSignatureValid(map, config.getKey());

        if (signatureValid) {
            if ("SUCCESS".equals(map.get("result_code"))) {
                //更新支付状态
//                orderService.

                //发送消息
                rabbitTemplate.convertAndSend("paynotify", "", map.get("out_trade_no"));
            } else {
                //记录日志
            }
        } else {
            //记录日志
        }
    }

    @Override
    public Map queryPayStatus(String orderId) {

        try {
            //1.封装参数
            Map<String, String> param = new HashMap<>();
            param.put("appid", config.getAppID());
            param.put("mch_id", config.getMchID());
            param.put("out_trade_no", orderId);
            param.put("nonce_str", WXPayUtil.generateNonceStr());
            String xmlParam = WXPayUtil.generateSignedXml(param, config.getKey());

            //2.调用接口
            WXPayRequest wxPayRequest = new WXPayRequest(config);
            String result = wxPayRequest.requestWithCert("/pay/orderquery", null, xmlParam, false);

            //3.解析结果
            Map<String, String> map = WXPayUtil.xmlToMap(result);
            return map;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }

    }
}
