package com.cxylk.dto;

import com.cxylk.nosql.elasticsearch.document.EsProduct;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * @Classname EsProductDap
 * @Description 搜索系统中的商品管理自定义Dao
 * @Author likui
 * @Date 2020/11/27 13:30
 **/
public interface EsProductDao {
    /**
     * 获取EsProduct列表集合
     * @param id id
     * @return
     */
    List<EsProduct> getAllEsProductList(@Param("id") Long id);
}
