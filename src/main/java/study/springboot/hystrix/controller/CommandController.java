package study.springboot.hystrix.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import study.springboot.hystrix.service.CommandService;
import study.springboot.hystrix.support.result.Result;
import study.springboot.hystrix.support.result.Results;

import java.util.Map;

@Slf4j
@RestController
@RequestMapping("/command")
public class CommandController {

    @Autowired
    private CommandService commandService;

    @GetMapping("/get")
    public Result get(String name) {
        log.info("aaaaaaaaa");
        Map<String, Object> data = commandService.get(name);
        log.info("bbbbbbbbbbb");
        return Results.success(data);
    }

    @GetMapping("/getBySync")
    public Result getUserInfoBySync(String name) {
        log.info("aaaaaaaaa");
        Map<String, Object> data = commandService.getBySync(name);
        log.info("bbbbbbbbbbb");
        return Results.success(data);
    }

    @GetMapping("/getByAsync")
    public Result getUserInfoByAsync(String userId) throws Exception {
//        UserInfo userInfo = commandService.getUserInfoByAsync(userId).get();
        return Results.success();
    }
}
