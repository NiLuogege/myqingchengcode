package com.qingcheng.service.impl;

import com.alibaba.dubbo.config.annotation.Service;
import com.qingcheng.dao.SkuMapper;
import com.qingcheng.dao.StockBackMapper;
import com.qingcheng.pojo.goods.StockBack;
import com.qingcheng.pojo.order.OrderItem;
import com.qingcheng.service.goods.StockBackService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.List;

@Service(interfaceClass = StockBackService.class)
public class StockBackServiceImpl implements StockBackService {

    @Autowired
    StockBackMapper stockBackMapper;

    @Autowired
    private SkuMapper skuMapper;

    @Transactional
    @Override
    public void addList(List<OrderItem> orderItemList) {
        for (OrderItem orderItem : orderItemList) {
            StockBack stockBack = new StockBack();
            stockBack.setSkuId(orderItem.getSkuId());
            stockBack.setOrderId(orderItem.getOrderId());
            stockBack.setStatus("0");
            stockBack.setNum(orderItem.getNum());
            stockBack.setCreateTime(new Date());

            stockBackMapper.insert(stockBack);

        }
    }

    @Transactional
    @Override
    public void doBack() {

        System.out.println("库存数据回滚");

        StockBack stockBack = new StockBack();
        stockBack.setStatus("0");
        List<StockBack> stockBacks = stockBackMapper.select(stockBack);

        for (StockBack back : stockBacks) {
            //回退库存
            skuMapper.deductionStock(back.getSkuId(), -back.getNum());

            //减少销量
            skuMapper.addSaleNum(back.getSkuId(), -back.getNum());

            back.setStatus("1");
            back.setBackTime(new Date());
            stockBackMapper.updateByPrimaryKey(back);
        }
        System.out.println("库存回滚任务结束");


    }
}
