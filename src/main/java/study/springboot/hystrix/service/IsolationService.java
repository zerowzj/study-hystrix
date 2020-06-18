package study.springboot.hystrix.service;

import com.netflix.hystrix.contrib.javanica.annotation.HystrixCommand;
import com.netflix.hystrix.contrib.javanica.annotation.HystrixProperty;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import study.springboot.hystrix.support.result.Results;

import java.util.Map;

@Slf4j
@Service("isolationService")
public class IsolationService {

    @HystrixCommand(commandProperties = {
            @HystrixProperty(name = "execution.isolation.strategy", value = "THREAD"),
            @HystrixProperty(name = "execution.isolation.thread.timeoutInMilliseconds", value = "60000"),
            @HystrixProperty(name = "execution.isolation.thread.interruptOnTimeout", value = "true"),
            @HystrixProperty(name = "execution.isolation.thread.interruptOnCancel", value = "true")},
            threadPoolKey = "ttt",
            threadPoolProperties = {
                    @HystrixProperty(name = "coreSize", value = "3")})
    public Map<String, Object> useThread() {
        log.info("safadsfasdfsaf");
        return Results.data();
    }

    @HystrixCommand(commandProperties = {
            @HystrixProperty(name = "execution.isolation.strategy", value = "SEMAPHORE"),
            @HystrixProperty(name = "execution.isolation.semaphore.maxConcurrentRequests", value = "100")})
    public Map<String, Object> useSemaphore() {

        return Results.data();
    }
}
