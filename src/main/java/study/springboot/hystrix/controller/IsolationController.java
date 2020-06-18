package study.springboot.hystrix.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import study.springboot.hystrix.service.IsolationService;
import study.springboot.hystrix.support.result.Result;
import study.springboot.hystrix.support.result.Results;

import java.util.Map;

@Slf4j
@RestController
@RequestMapping("/isolation")
public class IsolationController {

    @Autowired
    private IsolationService isolationService;

    @GetMapping("/useThread")
    public Result useThread() {
        log.info("aaaaaaaaaa");
        Map<String, Object> data = isolationService.useThread();
        log.info("bbbbbbbbbb");
        return Results.success(data);
    }

    @GetMapping("/useSemaphore")
    public Result useSemaphore() {
        Map<String, Object> data = isolationService.useSemaphore();
        return Results.success(data);
    }
}
