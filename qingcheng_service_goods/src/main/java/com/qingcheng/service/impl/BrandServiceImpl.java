package com.qingcheng.service.impl;

import com.qingcheng.dao.BrandMapper;
import com.qingcheng.pojo.goods.Brand;
import com.qingcheng.service.goods.BrandService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class BrandServiceImpl implements BrandService {

    @Autowired
    BrandMapper brandMapper;

    @Override
    public List<Brand> getAllBrand() {
        return brandMapper.getAllBrand();
    }

    public List<Brand> findAll() {
        return brandMapper.selectAll();
    }
}