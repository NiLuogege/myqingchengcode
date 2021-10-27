package com.qingcheng.task;

import com.alibaba.fastjson.JSON;
import com.qingcheng.dao.SeckillGoodsMapper;
import com.qingcheng.pojo.order.SeckillStatus;
import com.qingcheng.pojo.seckill.SeckillGoods;
import com.qingcheng.pojo.seckill.SeckillOrder;
import com.qingcheng.util.CacheKeyString;
import com.qingcheng.util.IdWorker;
import org.springframework.amqp.AmqpException;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.core.MessagePostProcessor;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;

import java.util.Date;

@Component
public class MultiThreadingCreateOrder {
    @Autowired
    RedisTemplate redisTemplate;

    @Autowired
    IdWorker idWorker;

    @Autowired
    SeckillGoodsMapper seckillGoodsMapper;

    @Autowired
    RabbitTemplate rabbitTemplate;

    @Async
    public void createOrder() throws InterruptedException {
        System.out.println("createOrder");

        //从队列中取出一个消息
        SeckillStatus seckillStatus = (SeckillStatus) redisTemplate.boundListOps("SeckillOrderQueue").rightPop();
        String username = seckillStatus.getUsername();
        String time = seckillStatus.getTime();
        Long id = seckillStatus.getGoodsId();


        //从队列中取出一个 商品id
        Object goodsId = redisTemplate.boundListOps("SeckillGoodsCountList_" + id).rightPop();
        //商品售罄时 清楚排队信息
        if (goodsId == null) {
            clearQueue(seckillStatus);
        }

        //从缓存中取出对应秒杀商品
        SeckillGoods goods = (SeckillGoods) redisTemplate.boundHashOps(CacheKeyString.seckill_goods + time).get(id);

        //有商品
        if (goods != null && goods.getStockCount() > 0) {


            //创建订单并保存到缓存中
            SeckillOrder seckillOrder = new SeckillOrder();
            seckillOrder.setId(idWorker.nextId());
            seckillOrder.setSeckillId(id);
            seckillOrder.setMoney(goods.getCostPrice());
            seckillOrder.setUserId(username);
            seckillOrder.setSellerId(goods.getSellerId());
            seckillOrder.setCreateTime(new Date());
            seckillOrder.setStatus("0");
            redisTemplate.boundHashOps("SeckillOrder").put(username, seckillOrder);

            //削减库存 并更新数据
            Long surplusCount = redisTemplate.boundHashOps("SeckillGoodsCount").increment(goods.getId(), -1);
            goods.setStockCount(surplusCount.intValue());
            if (goods.getStockCount() == 0) {//没有库存了，删除缓存数据，并同步到数据库
                redisTemplate.boundHashOps(CacheKeyString.seckill_goods + time).delete(id);
                seckillGoodsMapper.updateByPrimaryKeySelective(goods);
            } else {//更新数据
                redisTemplate.boundHashOps(CacheKeyString.seckill_goods + time).put(id, goods);
            }

            //更新秒杀单状态
            seckillStatus.setOrderId(seckillOrder.getId());
            seckillStatus.setMoney(seckillOrder.getMoney().floatValue());
            seckillStatus.setStatus(2);//抢单成功，待支付
            redisTemplate.boundHashOps("UserQueueStatus").put(username,seckillStatus);

            //发送消息到延时队列
            rabbitTemplate.convertAndSend("exchange.delay.order.begin", "delay", JSON.toJSONString(seckillStatus), new MessagePostProcessor() {
                @Override
                public Message postProcessMessage(Message message) throws AmqpException {
                    //设置消息过期时间
                    message.getMessageProperties().setExpiration(String.valueOf(10000));
                    return message;
                }
            });
        }
    }

    /***
     * 清理用户排队信息
     * @param seckillStauts
     */
    private void clearQueue(SeckillStatus seckillStauts) {
        //清理重复排队标识
        redisTemplate.boundHashOps("UserQueueCount").delete(seckillStauts.getUsername());

        //清理排队存储信息
        redisTemplate.boundHashOps("UserQueueStatus").delete(seckillStauts.getUsername());
    }
}
