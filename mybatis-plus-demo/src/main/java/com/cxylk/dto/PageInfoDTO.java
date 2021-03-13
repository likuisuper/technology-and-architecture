package com.cxylk.dto;

import lombok.Data;

import java.io.Serializable;
import java.util.List;

/**
 * @Classname PageInfoDTO
 * @Description 分页DTO
 * @Author likui
 * @Date 2021/2/3 15:24
 **/
@Data
public class PageInfoDTO implements Serializable {
    /**
     * 总记录数
     */
    private Integer totalCount;

    /**
     * 每页记录数
     */
    private Integer pageSize;

    /**
     * 总页数
     */
    private Integer totalPage;

    /**
     * 当前页数
     */
    private Integer currPage;

    /**
     * 列表数据
     */
    private List<?> list;

    public PageInfoDTO(List<?> list, int totalCount, int pageSize, int currPage){
        this.list=list;
        this.totalCount=totalCount;
        this.pageSize=pageSize;
        this.currPage=currPage;
        this.totalPage= (int) Math.ceil((double)totalCount/pageSize);
    }
}
