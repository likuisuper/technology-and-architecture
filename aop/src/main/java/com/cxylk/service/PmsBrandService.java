package com.cxylk.service;

import com.cxylk.mbg.model.PmsBrand;

import java.util.List;

/**
 * @Classname PmsBrandService
 * @Description TODO
 * @Author likui
 * @Date 2020/12/2 17:28
 **/
public interface PmsBrandService {
    List<PmsBrand> listAllBrand();

    int createBrand(PmsBrand brand);

    int updateBrand(Long id, PmsBrand brand);

    int deleteBrand(Long id);

    List<PmsBrand> listBrand(int pageNum, int pageSize);

    PmsBrand getBrand(Long id);
}
