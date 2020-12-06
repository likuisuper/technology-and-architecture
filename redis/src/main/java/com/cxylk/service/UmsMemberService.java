package com.cxylk.service;

import com.cxylk.common.api.CommonResult;

/**
 * @Classname UmsMemberService
 * @Description 会员管理Service
 * @Author likui
 * @Date 2020/12/6 15:29
 **/
public interface UmsMemberService {
    /**
     * 生成验证码
     * @param phone
     * @return
     */
    CommonResult<Object> generateAuthCode(String phone);

    /**
     * 判断验证码是否和手机匹配
     * @param phone
     * @param code
     * @return
     */
    CommonResult<Object> verifyAuthCode(String phone,String code);
}
