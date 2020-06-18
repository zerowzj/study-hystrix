package study.springboot.hystrix.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import study.springboot.hystrix.service.FallbackService;
import study.springboot.hystrix.support.result.Result;
import study.springboot.hystrix.support.result.Results;

import java.util.Map;

@RestController
@RequestMapping("/fallback")
public class FallbackController {

    @Autowired
    private FallbackService fallbackService;

    /**
     * 超时回退
     */
    @GetMapping("/timeout")
    public Result timeout(Long timeout) {
        Map<String, Object> data = fallbackService.timeout(timeout);
        return Results.success(data);
    }

    /**
     * 异常回退
     */
    @GetMapping("/exception")
    public Result exception() {
        Map<String, Object> data = fallbackService.exception();
        return Results.success(data);
    }
}
