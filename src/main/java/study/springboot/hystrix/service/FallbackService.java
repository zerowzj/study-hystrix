package study.springboot.hystrix.service;

import com.netflix.hystrix.contrib.javanica.annotation.HystrixCommand;
import com.netflix.hystrix.contrib.javanica.annotation.HystrixProperty;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import study.springboot.hystrix.support.Sleeps;

@Slf4j
@Service("fallbackService")
public class FallbackService {

    @HystrixCommand(commandProperties = {
            @HystrixProperty(name = "fallback.enabled", value = "true"),
            @HystrixProperty(name = "execution.timeout.enabled", value = "true")})
    public void timeout(Long timeout) {
        Sleeps.seconds(timeout);
    }

    @HystrixCommand(commandProperties = {
            @HystrixProperty(name = "fallback.enabled", value = "true"),
            @HystrixProperty(name = "", value = "")})
    public void exception() {

    }

    public void defaultFallback() {

    }
}
