package com.qingcheng.consumer;

import com.alibaba.fastjson.JSON;
import com.qingcheng.dao.SeckillGoodsMapper;
import com.qingcheng.pojo.order.SeckillStatus;
import com.qingcheng.pojo.seckill.SeckillGoods;
import com.qingcheng.pojo.seckill.SeckillOrder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.connection.Message;
import org.springframework.data.redis.connection.MessageListener;
import org.springframework.data.redis.core.RedisTemplate;

/**
 * 秒杀订单回滚
 */
public class SeckillOrderMessageListener implements MessageListener {
    @Autowired
    private RedisTemplate redisTemplate;

    @Autowired
    private SeckillGoodsMapper seckillGoodsMapper;

    @Override
    public void onMessage(Message message, byte[] bytes) {
        String content = new String(message.getBody());
        rollbackOrder(JSON.parseObject(content, SeckillStatus.class));
    }

    public void rollbackOrder(SeckillStatus seckillStatus) {
        if (seckillStatus == null) {
            return;
        }

        //从缓存中找到 用户对应的秒杀订单
        SeckillOrder seckillOrder = (SeckillOrder) redisTemplate.boundHashOps("SeckillOrder").get(seckillStatus.getUsername());
        if (seckillOrder != null) {

            //先关闭微信支付，成功后在进行回滚
//            boolean isSuccess = wxxinPayService.closePay(seckillStatus.getOrderId().toString());
//            if (isSuccess){


            //删除用户订单
            redisTemplate.boundHashOps("SeckillOrder").delete(seckillOrder.getUserId());


            //查询对应商品
            SeckillGoods goods = (SeckillGoods) redisTemplate.boundHashOps("SeckillGoods_" + seckillStatus.getTime()).get(seckillStatus.getGoodsId());
            // 之前已经被删掉了，需要重新加入到缓存
            if (goods == null)
                goods = seckillGoodsMapper.selectByPrimaryKey(seckillStatus.getGoodsId());

            Long seckillGoodsCount = redisTemplate.boundHashOps("SeckillGoodsCount").increment(seckillStatus.getGoodsId(), 1);
            goods.setStockCount(seckillGoodsCount.intValue());

            redisTemplate.boundHashOps("SeckillGoods_" + seckillStatus.getTime()).put(seckillStatus.getGoodsId(), goods);
            redisTemplate.boundListOps("SeckillGoodsCountList_" + seckillStatus.getGoodsId()).leftPush(seckillStatus.getGoodsId());

            redisTemplate.boundHashOps("UserQueueCount").delete(seckillStatus.getUsername());
            redisTemplate.boundHashOps("UserQueueStatus").delete(seckillStatus.getUsername());

//            }
        }

    }
}
