package com.qingcheng.service.impl;

import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.qingcheng.dao.BrandMapper;
import com.qingcheng.entity.PageResult;
import com.qingcheng.pojo.goods.Brand;
import com.qingcheng.service.goods.BrandService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import tk.mybatis.mapper.entity.Example;

import java.util.List;
import java.util.Map;

@Service
public class BrandServiceImpl implements BrandService {

    @Autowired
    BrandMapper brandMapper;

    @Override
    public List<Brand> getAllBrand() {
        return brandMapper.getAllBrand();
    }

    @Override
    public List<Brand> findAll() {
        return brandMapper.selectAll();
    }

    @Override
    public PageResult<Brand> findPage(int page, int size) {
        PageHelper.startPage(page, size);
        Page<Brand> pageResult = (Page<Brand>) brandMapper.selectAll();
        return new PageResult<Brand>(pageResult.getTotal(), pageResult.getResult());
    }

    @Override
    public PageResult<Brand> findPage(Map searchMap, int page, int size) {
        PageHelper.startPage(page, size);
        Example example = createExample(searchMap);
        Page<Brand> pageResult = (Page<Brand>) brandMapper.selectByExample(example);
        return new PageResult<Brand>(pageResult.getTotal(), pageResult.getResult());
    }

    private Example createExample(Map searchMap) {
        Example example = new Example(Brand.class);
        Example.Criteria criteria = example.createCriteria();

        System.out.println("searchMap" + searchMap.toString());

        if (searchMap != null) {
            Object name = searchMap.get("name");
            if (name != null && "".equals(name)) {
                criteria.andLike("name", "%" + name + "%");
            }
            Object letter = searchMap.get("letter");
            if (letter != null && "".equals(letter)) {
                criteria.andEqualTo("name", letter);
            }

        }
        return example;
    }

}
