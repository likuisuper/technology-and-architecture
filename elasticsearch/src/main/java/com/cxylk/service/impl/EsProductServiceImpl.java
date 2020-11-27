package com.cxylk.service.impl;

import cn.hutool.core.collection.CollectionUtil;
import com.cxylk.dao.EsProductDao;
import com.cxylk.nosql.elasticsearch.document.EsProduct;
import com.cxylk.nosql.elasticsearch.repository.EsProductRepository;
import com.cxylk.service.EsProductService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

/**
 * @Classname EsProductServiceImpl
 * @Description 商品搜索管理Service实现类
 * @Author likui
 * @Date 2020/11/27 13:59
 **/
@Service
public class EsProductServiceImpl implements EsProductService {
    private static final Logger LOGGER= LoggerFactory.getLogger(EsProductServiceImpl.class);

    @Autowired
    private EsProductDao productDao;

    @Autowired
    private EsProductRepository productRepository;

    @Override
    public int importAll() {
        List<EsProduct> esProducts=productDao.getAllEsProductList(null);
        Iterable<EsProduct> esProductIterable=productRepository.saveAll(esProducts);
        Iterator<EsProduct> iterator=esProductIterable.iterator();
        int result=0;
        while (iterator.hasNext()){
            result++;
            iterator.next();
        }
        return result;
    }

    @Override
    public void delete(Long id) {
        productRepository.deleteById(id);
    }

    @Override
    public EsProduct create(Long id) {
        EsProduct result=null;
        List<EsProduct> esProductList=productDao.getAllEsProductList(id);
        if(esProductList.size()>0){
            EsProduct esProduct=esProductList.get(0);
            result=productRepository.save(esProduct);
        }
        return result;
    }

    @Override
    public void delete(List<Long> ids) {
        if(!CollectionUtil.isEmpty(ids)){
            List<EsProduct> esProductList=new ArrayList<>();
            for (Long id : ids) {
                EsProduct esProduct=new EsProduct();
                esProduct.setId(id);
                esProductList.add(esProduct);
            }
            productRepository.deleteAll(esProductList);
        }
    }

    @Override
    public Page<EsProduct> search(String keyword, Integer pageNum, Integer pageSize) {
        Pageable pageable=PageRequest.of(pageNum,pageSize);
        return productRepository.findByNameOrSubTitleOrKeywords(keyword,keyword,keyword,pageable);
    }
}