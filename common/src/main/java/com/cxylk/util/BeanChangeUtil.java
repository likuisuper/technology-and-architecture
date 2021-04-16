package com.cxylk.util;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.beans.PropertyDescriptor;
import java.lang.reflect.Field;
import java.lang.reflect.Method;

/**
 * @Classname BeanChangeUtil
 * @Description 获取两个bean中变化的属性字段
 * @Author likui
 * @Date 2021/4/16 13:22
 **/
@Component
@Slf4j
public class BeanChangeUtil<T> {
    @SuppressWarnings({"unchecked", "rawtypes"})
    public String contrastObj(Object oldBean, Object newBean) {
        StringBuilder str = new StringBuilder();
        T pojo1 = (T) oldBean;
        T pojo2 = (T) newBean;
        try {
            // 通过反射获取类的类类型及字段属性
            Class clazz = pojo1.getClass();
            Field[] fields = clazz.getDeclaredFields();
            int i = 1;
            for (Field field : fields) {
                // 排除序列化属性
                if ("serialVersionUID".equals(field.getName())) {
                    continue;
                }
                PropertyDescriptor pd = new PropertyDescriptor(field.getName(), clazz);
                // 获取对应属性值
                Method getMethod = pd.getReadMethod();
                Object o1 = getMethod.invoke(pojo1);
                Object o2 = getMethod.invoke(pojo2);
                if (ComUtil.isEmpty(o1) && ComUtil.isEmpty(o2)) {
                    continue;
                }
                if (!ComUtil.isEmpty(o1) && ComUtil.isEmpty(o2)) {
                    str.append(i + ".字段:" + field.getName() + ",原值:" + o1 + ",改为:" + "空值" + ";");
                    i++;
                }
                if (ComUtil.isEmpty(o1) && !ComUtil.isEmpty(o2)) {
                    str.append(i + ".字段:" + field.getName() + ",原值:" + "空值" + ",改为:" + o2 + ";");
                    i++;
                }
                if (!ComUtil.isEmpty(o1) && !ComUtil.isEmpty(o2) && !o1.toString().equals(o2.toString())) {
                    str.append(i + ".字段:" + field.getName() + ",原值:" + o1 + ",改为:" + o2 + ";");
                    i++;
                }
            }
        } catch (Exception e) {
            log.error(e.getMessage(), e);
        }
        return str.toString();
    }
}
