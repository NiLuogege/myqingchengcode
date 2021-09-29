package com.qingcheng.service.goods;

import com.qingcheng.entity.PageResult;
import com.qingcheng.pojo.goods.Brand;

import java.util.List;

public interface BrandService {

    List<Brand> getAllBrand();

    public List<Brand> findAll();

    public PageResult<Brand> findPage(int page, int size);

}
