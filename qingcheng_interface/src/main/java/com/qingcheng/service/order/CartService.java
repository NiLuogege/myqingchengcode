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

    /**
     * 修改 选中
     * @param username
     * @param skuId
     * @param check
     * @return
     */
    boolean updateCheck (String username , String skuId, boolean check);


    /**
     * 计算优惠
     * @param username
     * @return
     */
    int preferential(String username) ;

    /**
     * 更新购物车缓存数据
     * @param username
     * @return
     */
    List<Map<String, Object>> findNewOrderItemList(String username);

    /**
     * 删除选中的购物车
     * @param username
     */
    public void deleteCheckedCart(String username);
}
