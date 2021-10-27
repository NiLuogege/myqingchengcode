package com.qingcheng.consumer;

import com.alibaba.fastjson.JSON;
import com.qingcheng.pojo.order.SeckillStatus;
import com.qingcheng.pojo.seckill.SeckillOrder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.connection.Message;
import org.springframework.data.redis.connection.MessageListener;
import org.springframework.data.redis.core.RedisTemplate;

/**
 * 秒杀订单回滚
 */
public class SeckillOrderMessageListener  implements MessageListener {
    @Autowired
    private RedisTemplate redisTemplate;

    @Override
    public void onMessage(Message message, byte[] bytes) {
        String content = new String(message.getBody());
        rollbackOrder(JSON.parseObject(content,SeckillStatus.class));
    }

    public void rollbackOrder(SeckillStatus seckillStatus){
        if (seckillStatus==null){
            return;
        }

        //从缓存中找到 用户对应的秒杀订单
        SeckillOrder seckillOrder = (SeckillOrder) redisTemplate.boundHashOps("SeckillOrder").get(seckillStatus.getUsername());
        if (seckillOrder!=null){

        }

    }
}
