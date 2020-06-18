package study.springboot.hystrix.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import study.springboot.hystrix.service.FallbackService;
import study.springboot.hystrix.support.result.Result;

@RestController
@RequestMapping("/fallback")
public class FallbackController {

    @Autowired
    private FallbackService fallbackService;

    @GetMapping("/timeout")
    public Result timeout() {

        return null;
    }

    @GetMapping("/exception")
    public Result exception() {

        return null;
    }
}
