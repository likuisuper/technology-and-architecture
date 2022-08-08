package com.cxylk.reflectionannotationtype;

import java.util.Arrays;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * @author likui
 * @date 2022/8/5 下午2:01
 * 定义⼀个泛型⽗类，并
 * 在⽗类中定义⼀个统⼀的⽇志记录⽅法，⼦类可以通过继承重⽤这个⽅法，类字段内容变动时记录⽇志
 **/
public class Parent<T> {
    //⽤于记录value更新的次数，模拟⽇志记录的逻辑
    AtomicInteger updateCount = new AtomicInteger();

    private T value;

    /**
     * 设置值
     *
     * @param value
     */
    public void setValue(T value) {
        System.out.println("Parent.setValue called");
        this.value = value;
        updateCount.incrementAndGet();
    }

    //重写toString，输出值和值更新次数
    @Override
    public String toString() {
        return String.format("value: %s updateCount: %d", value, updateCount.get());
    }

    public static void main(String[] args) {
//        Child1 child1 = new Child1();
//        //使用getDeclaredMethods()方法可以只获取子类的setValue，实现正确输出
//        //但这治标不治本，因为还是存在两个setValue
//        Arrays.stream(child1.getClass().getMethods())
//                .filter(method -> method.getName().equals("setValue"))
//                .forEach(method -> {
//                    try {
//                        method.invoke(child1, "test");
//                    } catch (Exception e) {
//                        e.printStackTrace();
//                    }
//                });
//        System.out.println(child1.toString());

        //debug可以发现，即使使用了getDeclaredMethods()方法，还是
        //获取到了child2的两个setvalue方法，一个参数为String，一个参数为Object
        //为什么会有一个Object参数的方法？虽然子类指定了类型为String，但泛型擦除后变为了
        //Object，子类如果要覆盖父类的setValue方法，那么入参也必须是Object，于是编译器就
        //为我们生成了一个bridge桥接方法。可以通过javap查看
//        Child2 child2 = new Child2();
//        Arrays.stream(child2.getClass().getDeclaredMethods())
//                .filter(method -> method.getName().equals("setValue"))
//                .forEach(method -> {
//                    try {
//                        method.invoke(child2, "test");
//                    } catch (Exception e) {
//                        e.printStackTrace();
//                    }
//                });
//        System.out.println(child2.toString());

        //过滤掉bridge方法
        Child2 child2 = new Child2();
        Arrays.stream(child2.getClass().getDeclaredMethods())
                .filter(method -> method.getName().equals("setValue") && !method.isBridge())
                .findFirst().ifPresent(method -> {
            try {
                method.invoke(child2, "test");
            } catch (Exception e) {
                e.printStackTrace();
            }
        });
        System.out.println(child2.toString());
    }

    static class Child1 extends Parent<String> {
        //1、没有带上@overrivide注解，父类的方法中，参数经过泛型擦除后变为Object类型了
        //结果就是setValue被调用了两次
        //Child1.setValue called
        //Parent.setValue called
        //Parent.setValue called
        //value: test updateCount: 2
        public void setValue(String value) {
            System.out.println("Child1.setValue called");
            super.setValue(value);
        }
    }

    /**
     * 2、指明类型为String，并正确重写父类方法
     */
    static class Child2 extends Parent<String> {
        @Override
        public void setValue(String value) {
            System.out.println("Child2.setValue called");
            super.setValue(value);
        }
    }
}
