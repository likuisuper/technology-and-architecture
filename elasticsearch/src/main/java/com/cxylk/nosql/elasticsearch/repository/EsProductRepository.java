package com.cxylk.nosql.elasticsearch.repository;

import com.cxylk.nosql.elasticsearch.document.EsProduct;
import com.github.pagehelper.PageHelper;
import org.springframework.data.domain.Page;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

/**
 * @Classname EsProductRepository
 * @Description 商品ES操作类。继承ElasticsearchRepository接口，这样就拥有了一些基本的Elasticsearch数据操作方法，
 *              同时定义了一个衍生查询方法。
 * @Author likui
 * @Date 2020/11/27 13:22
 **/
public interface EsProductRepository extends ElasticsearchRepository<EsProduct,Long> {
    /**
     * 搜索查询
     * @param name 商品名称
     * @param subTitle 商品标题
     * @param keywords 商品关键字
     * @param helper 分页信息
     * @return
     */
    Page<EsProduct> findByNameOrSubTitleOrKeywords(String name, String subTitle, String keywords, PageHelper helper);
}