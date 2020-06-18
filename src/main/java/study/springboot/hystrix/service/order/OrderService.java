package study.springboot.hystrix.service.order;

import com.netflix.hystrix.contrib.javanica.annotation.HystrixCommand;
import com.netflix.hystrix.contrib.javanica.annotation.HystrixProperty;
import org.springframework.stereotype.Service;

import java.util.List;

@Service("orderService")
public class OrderService {

    @HystrixCommand(commandProperties = {@HystrixProperty(name = "execution.isolation.strategy", value = "THREAD"),
            @HystrixProperty(name = "execution.timeout.enabled", value = "true"),
            @HystrixProperty(name = "execution.isolation.thread.timeoutInMilliseconds", value = "60000"),
            @HystrixProperty(name = "execution.isolation.thread.interruptOnTimeout", value = "true"),
            @HystrixProperty(name = "execution.isolation.thread.interruptOnCancel", value = "true"),
            @HystrixProperty(name = "execution.isolation.semaphore.maxConcurrentRequests", value = "100")})
    public OrderInfo getOrderInfo() {
        return null;
    }

    @HystrixCommand(threadPoolProperties = {@HystrixProperty(name = "coreSize", value = "10"),
            @HystrixProperty(name = "allowMaximumSizeToDivergeFromCoreSize", value = "false"),
            @HystrixProperty(name = "maximumSize", value = "20"),
            @HystrixProperty(name = "maxQueueSize", value = "-1"),
            @HystrixProperty(name = "queueSizeRejectionThreshold", value = ""),
            @HystrixProperty(name = "keepAliveTimeMinutes", value = "")})
    public List<OrderInfo> getOrderLt() {
        return null;
    }
}
