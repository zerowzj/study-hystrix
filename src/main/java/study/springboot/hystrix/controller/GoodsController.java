package study.springboot.hystrix.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import study.springboot.hystrix.service.order.OrderInfo;
import study.springboot.hystrix.service.order.OrderService;
import study.springboot.hystrix.support.result.Result;

@Slf4j
@RestController
public class GoodsController {

    @Autowired
    private OrderService orderService;

    @GetMapping("/getOrderInfo")
    public Result getOrderInfo(String orderNo) {
        log.info("aaaaaaaaa");
        OrderInfo orderInfo = orderService.getOrderInfo(orderNo);
        return new Result(orderInfo);
    }

    @GetMapping("/getOrderLt")
    public Result getOrderLt(String name) throws Exception {
        return new Result();
    }
}
