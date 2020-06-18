package study.springboot.hystrix.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import study.springboot.hystrix.service.order.OrderService;
import study.springboot.hystrix.service.user.UserInfo;
import study.springboot.hystrix.service.user.UserService;
import study.springboot.hystrix.support.result.Result;

@Slf4j
@RestController
public class OrderController {

    @Autowired
    private OrderService orderService;

    @GetMapping("/getOrderInfo")
    public Result getOrderInfo(String orderNo) {
        log.info("aaaaaaaaa");

        return new Result();
    }

    @GetMapping("/getOrderLt")
    public Result getOrderLt(String name) throws Exception {
        return new Result();
    }
}
