package com.cxylk.domain;

import io.swagger.annotations.ApiModelProperty;
import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;

import javax.naming.ldap.PagedResultsControl;
import java.util.List;

/**
 * @Classname PlayloadDto
 * @Description 封装jwt中存放的信息
 * @Author likui
 * @Date 2020/11/26 10:24
 **/
@Data
@EqualsAndHashCode(callSuper = false)
@Builder
public class PlayloadDto {
    @ApiModelProperty("主题")
    private String sub;
    @ApiModelProperty("签发时间")
    private Long iat;
    @ApiModelProperty("过期时间")
    private Long exp;
    @ApiModelProperty("JWT的ID(唯一身份标识)")
    private String jti;
    @ApiModelProperty("用户名称")
    private String username;
    @ApiModelProperty("用户拥有的权限")
    private List<String> authorities;
}
