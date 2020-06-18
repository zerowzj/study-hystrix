package study.springboot.hystrix.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import study.springboot.hystrix.service.user.UserInfo;
import study.springboot.hystrix.service.user.UserService;
import study.springboot.hystrix.support.result.Result;

@Slf4j
@RestController
public class UserController {

    @Autowired
    private UserService userService;

    @GetMapping("/getUserInfo")
    public Result getUserInfo(String userId) {
        log.info("aaaaaaaaa");
        UserInfo userInfo = userService.getUserInfo(userId);
        log.info("bbbbbbbbbbb");
        return new Result(userInfo);
    }

    @GetMapping("/getUserInfoBySync")
    public Result getUserInfoBySync(String userId) {
        log.info("aaaaaaaaa");
        UserInfo userInfo = userService.getUserInfoBySync(userId);
        log.info("bbbbbbbbbbb");
        return new Result(userInfo);
    }

    @GetMapping("/getUserInfoByAsync")
    public Result getUserInfoByAsync(String userId) throws Exception {
        UserInfo userInfo = userService.getUserInfoByAsync(userId).get();
        return new Result();
    }
}
