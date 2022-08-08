package com.cxylk.spring.iocandaop;

import lombok.extern.slf4j.Slf4j;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

/**
 * @author likui
 * @date 2022/8/5 下午3:15
 * 定义一个有状态的bean
 **/
@Slf4j
public abstract class SayService {
    List<String> data = new ArrayList<>();
    public void say() {
        data.add(IntStream.rangeClosed(1, 1000000)
                .mapToObj(__ -> "a")
                .collect(Collectors.joining("")) + UUID.randomUUID().toString());
        log.info("I'm {} size:{}", this, data.size());
    }
}
