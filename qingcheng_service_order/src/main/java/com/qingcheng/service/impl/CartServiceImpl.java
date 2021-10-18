package com.qingcheng.service.impl;

import com.alibaba.dubbo.config.annotation.Reference;
import com.alibaba.dubbo.config.annotation.Service;
import com.qingcheng.pojo.goods.Category;
import com.qingcheng.pojo.goods.Sku;
import com.qingcheng.pojo.order.OrderItem;
import com.qingcheng.service.goods.CategoryService;
import com.qingcheng.service.goods.SkuService;
import com.qingcheng.service.order.CartService;
import com.qingcheng.util.CacheKey;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class CartServiceImpl implements CartService {

    @Autowired
    private RedisTemplate redisTemplate;

    @Reference
    private SkuService skuService;
    @Reference
    private CategoryService categoryService;

    /**
     * 从缓存中 获取购物车信息，因为 缓存的速度快，购物车操作频繁且快速
     *
     * @param username
     * @return
     */
    @Override
    public List<Map<String, Object>> findCartList(String username) {
        System.out.println("findCartList username="+username);

        List<Map<String, Object>> result = (List<Map<String, Object>>) redisTemplate.boundHashOps(CacheKey.CARD_LIST).get(username);
        if (result == null) {
            result = new ArrayList<>();
        }
        return result;
    }

    /**
     * 添加商品信息到缓存中，所以说 购物车中的信息 有可能可 数据库中的信息 不一致
     *
     * @param username
     * @param skuId
     * @param num
     */
    @Override
    public void addItem(String username, String skuId, int num) {


        boolean isHave = false;//是否已经加入到了购物车
        List<Map<String, Object>> cacheList = (List<Map<String, Object>>) redisTemplate.boundHashOps(CacheKey.CARD_LIST).get(username);
        if (cacheList == null) {
            cacheList = new ArrayList<>();
        }


        System.out.println("username="+username+" addItem="+cacheList.toString());

        for (Map<String, Object> cardItem : cacheList) {
            OrderItem orderItem = (OrderItem) cardItem.get("item");
            if (orderItem.getSkuId().equals(skuId)) {//已经存在，对已有的数据进行修改

                int widget = orderItem.getWeight() / orderItem.getNum();//每个商品的重量
                orderItem.setNum(num);
                orderItem.setWeight(widget * num);
                orderItem.setMoney(orderItem.getPrice() * num);

                if (orderItem.getNum() <= 0) {//数量非法，直接删除
                    cacheList.remove(orderItem);
                }

                isHave = true;
                break;
            }
        }

        if (!isHave) {//如果没有添加过
            Sku sku = skuService.findById(skuId);

            if (sku == null) {
                throw new RuntimeException("商品不存在");
            }

            if (!"1".equals(sku.getStatus())) {
                throw new RuntimeException("商品状态不合法");
            }

            if (num <= 0) {
                throw new RuntimeException("商品数量不合法");
            }

            List<Map<String, Object>> items = new ArrayList<>();
            Map<String, Object> item = new HashMap();
            OrderItem orderItem = new OrderItem();
            orderItem.setSkuId(skuId);
            orderItem.setNum(num);
            orderItem.setSpuId(sku.getSpuId());
            orderItem.setPrice(sku.getPrice());
            orderItem.setName(sku.getName());
            orderItem.setImage(sku.getImage());
            if (sku.getWeight() == null) {
                orderItem.setWeight(0);
            } else {
                orderItem.setWeight(sku.getWeight() * num);
            }


            //设置三级分类
            orderItem.setCategoryId3(sku.getCategoryId());
            Category category3 = (Category) redisTemplate.boundHashOps(CacheKey.CATEGROY).get(sku.getCategoryId());
            if (category3==null){
                category3= categoryService.findById(sku.getCategoryId());
                redisTemplate.boundHashOps(CacheKey.CATEGROY).put(sku.getCategoryId(),category3);
            }

            //设置二级分类
            orderItem.setCategoryId2(category3.getParentId());
            Category category2 = (Category) redisTemplate.boundHashOps(CacheKey.CATEGROY).get(category3.getParentId());
            if (category2==null){
                category2= categoryService.findById(category3.getParentId());
                redisTemplate.boundHashOps(CacheKey.CATEGROY).put(sku.getCategoryId(),category2);
            }

            //设置一级分类
            orderItem.setCategoryId1(category2.getParentId());

            item.put("item", orderItem);
            items.add(item);

            redisTemplate.boundHashOps(CacheKey.CARD_LIST).put(username, items);
        }
    }
}
