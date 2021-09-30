package com.qingcheng.service.goods;

import com.qingcheng.entity.PageResult;
import com.qingcheng.pojo.goods.Brand;

import java.util.List;
import java.util.Map;

public interface BrandService {

    List<Brand> getAllBrand();

    public List<Brand> findAll();

    public PageResult<Brand> findPage(int page, int size);

    public PageResult<Brand> findPage(Map searchMap, int page, int size);

    Brand findById(int id);
}
