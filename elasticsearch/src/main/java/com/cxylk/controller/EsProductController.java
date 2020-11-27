package com.cxylk.controller;

import com.cxylk.common.api.CommonPage;
import com.cxylk.common.api.CommonResult;
import com.cxylk.nosql.elasticsearch.document.EsProduct;
import com.cxylk.service.EsProductService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * @Classname EsProductController
 * @Description 商品搜索管理Controller
 * @Author likui
 * @Date 2020/11/27 14:39
 **/
@Api(tags = "商品搜索管理")
@RestController
@RequestMapping("/esProduct")
public class EsProductController {
    @Autowired
    private EsProductService esProductService;

    @ApiOperation(value = "导入数据库中商品到ES")
    @RequestMapping(value = "/importAll",method = RequestMethod.POST)
    public CommonResult<Integer> importAllList(){
        int count=esProductService.importAll();
        return CommonResult.success(count);
    }

    @ApiOperation(value = "根据id删除商品")
    @RequestMapping(value = "/delete/{id}",method = RequestMethod.GET)
    public CommonResult<Object> delete(@PathVariable Long id){
        esProductService.delete(id);
        return CommonResult.success(null);
    }

    @ApiOperation(value = "根据id批量删除")
    @RequestMapping(value = "/delete/batch",method = RequestMethod.POST)
    public CommonResult<Object> delete(@RequestParam("ids") List<Long> ids){
        esProductService.delete(ids);
        return CommonResult.success(null);
    }

    @ApiOperation(value = "根据id创建商品")
    @RequestMapping(value = "/create/{id}",method = RequestMethod.POST)
    public CommonResult<EsProduct> create(@PathVariable Long id){
        EsProduct esProduct=esProductService.create(id);
        if(esProduct!=null){
            return CommonResult.success(esProduct);
        }else {
            return CommonResult.failed();
        }
    }

    @ApiOperation(value = "实现简单搜索")
    @RequestMapping(value = "/simple/search",method = RequestMethod.GET)
    public CommonResult<CommonPage<EsProduct>> simpleSearch(@RequestParam(required = false) String keyword,
                                     @RequestParam(required = false,defaultValue = "0") Integer pageNum,
                                     @RequestParam(required = false,defaultValue = "5") Integer pageSize){
        //search方法中将findByNameOrSubTitleOrKeywords()的参数设置为keyword,那么es就会根据满足任意一个条件的参数进行搜索
        Page<EsProduct> products=esProductService.search(keyword,pageNum,pageSize);
        return CommonResult.success(CommonPage.restPage(products));
    }
}
