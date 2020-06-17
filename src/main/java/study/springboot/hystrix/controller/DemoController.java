package study.springboot.hystrix.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import study.springboot.hystrix.service.DemoService;

@Slf4j
@RestController
public class DemoController {

    @Autowired
    private DemoService demoService;

    @GetMapping("/sayHi")
    public String sayHi(String name) {
        return demoService.sayHi();
    }
}
