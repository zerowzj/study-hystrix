package study.springboot.hystrix.service;

import com.netflix.hystrix.contrib.javanica.annotation.HystrixCommand;
import com.netflix.hystrix.contrib.javanica.annotation.HystrixProperty;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import study.springboot.hystrix.support.Sleeps;
import study.springboot.hystrix.support.result.Results;

import java.util.Map;

@Slf4j
@Service("fallbackService")
public class FallbackService {

    @HystrixCommand(fallbackMethod = "tt", commandProperties = {
            @HystrixProperty(name = "fallback.enabled", value = "true"),
            @HystrixProperty(name = "execution.timeout.enabled", value = "true")})
    public Map<String, Object> timeout(Long timeout) {
        log.info("");
        Sleeps.seconds(timeout);

        return Results.data();
    }

    @HystrixCommand(ignoreExceptions = {RuntimeException.class},
            commandProperties = {
                    @HystrixProperty(name = "fallback.enabled", value = "true")})
    public Map<String, Object> exception() {
        log.info("");

        Map<String, Object> data = Results.data();
        return data;
    }

    public Map<String, Object> tt(Long timeout, Throwable ex){
        log.error("123123", ex);
        return null;
    }
}
