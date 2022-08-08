package com.cxylk.reflectionannotationtype;

import org.junit.Test;

/**
 * @author likui
 * @date 2022/8/5 上午10:55
 **/
public class TestMain {
    /**
     * 不使用反射，走哪个重载方法通过参数可以很清晰的知道
     */
    @Test
    public void test1(){
        ReflectionIssueApplication application=new ReflectionIssueApplication();
        application.age(36);
        application.age(Integer.valueOf(36));
    }


}
