package study.springboot.hystrix.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import study.springboot.hystrix.service.user.UserInfo;
import study.springboot.hystrix.service.user.UserService;
import study.springboot.hystrix.support.result.Result;

import java.util.concurrent.Future;

@Slf4j
@RestController
public class UserController {

    @Autowired
    private UserService userService;

    @GetMapping("/getUser")
    public Result getUser(String name) {
        log.info("aaaaaaaaa");
        UserInfo user = userService.getUser(null);
        log.info("bbbbbbbbbbb");
        return new Result(user);
    }

    @GetMapping("/getUserByAsync")
    public Result getUserByAsync(String name) {
        Future<UserInfo> future = userService.getUserByAsync("");
        return new Result();
    }
}
