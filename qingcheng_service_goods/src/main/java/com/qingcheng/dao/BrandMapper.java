package com.qingcheng.dao;

import com.qingcheng.pojo.goods.Brand;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

public interface BrandMapper {

    List<Brand> getAllBrand();

}
