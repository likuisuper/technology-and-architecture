package com.cxylk.spring.iocandaop;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * @author likui
 * @date 2022/8/5 下午3:18
 **/
@RequestMapping("/debug")
@RestController
@Slf4j
public class DebugController {
    @Autowired
    List<SayService> sayServiceList;

    @GetMapping("test")
    public void test() {
        log.info("====================");
        sayServiceList.forEach(SayService::say);
    }
}
