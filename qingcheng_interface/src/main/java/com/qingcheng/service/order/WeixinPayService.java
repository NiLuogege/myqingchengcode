package com.qingcheng.service.order;

import java.util.Map;

public interface WeixinPayService {

    /**
     * 生成微信支付二维码
     *
     * @param orderId   订单号
     * @param money     金额(分)
     * @param notifyUrl 回调地址
     * @return
     */
    public Map createNative(String orderId, Integer money, String notifyUrl) throws Exception;

    /**
     * 微信支付回调
     * @param xml
     */
    public void notifyLogic(String xml) throws Exception;

    /**
     * 查询支付结果
     * @param orderId
     * @return
     */
    public Map queryPayStatus(String orderId);
}
