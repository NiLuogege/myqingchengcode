package com.qingcheng.service.order;

import java.util.List;
import java.util.Map;

public interface CartService {

    /**
     * 获取购物车的
     * @param username
     * @return
     */
    List<Map<String,Object>> findCartList(String username);

    /**
     * 加入购物车
     * @param username
     * @param skuId
     * @param num
     */
    void  addItem (String username , String skuId,int num);

}
