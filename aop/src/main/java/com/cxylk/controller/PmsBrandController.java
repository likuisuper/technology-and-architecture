package com.cxylk.controller;

import com.cxylk.common.api.CommonPage;
import com.cxylk.common.api.CommonResult;
import com.cxylk.common.api.ResultCode;
import com.cxylk.mbg.model.PmsBrand;
import com.cxylk.service.PmsBrandService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import io.swagger.models.auth.In;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * @Classname PmsBrandController
 * @Description TODO
 * @Author likui
 * @Date 2020/12/2 17:31
 **/
@Api(value = "PmsBrandController",tags = "品牌管理")
@RestController
@RequestMapping("/brand")
public class PmsBrandController {
    @Autowired
    private PmsBrandService pmsBrand;

    @ApiOperation(value = "分页查询品牌管理")
    @RequestMapping(value = "/list",method = RequestMethod.GET)
    public CommonResult<CommonPage<PmsBrand>> listBrand(@RequestParam(value = "pageNum",defaultValue = "1")
                                          @ApiParam("页码") Integer pageNum,
                                          @RequestParam(value = "pageSize",defaultValue = "5")
                                          @ApiParam("每页数量") Integer pageSize){
        List<PmsBrand> list=pmsBrand.listBrand(pageNum,pageSize);
        return CommonResult.success(CommonPage.restPage(list));
    }
}
