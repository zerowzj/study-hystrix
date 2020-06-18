package study.springboot.hystrix.service.order;

import com.netflix.hystrix.contrib.javanica.annotation.HystrixCommand;
import com.netflix.hystrix.contrib.javanica.annotation.HystrixProperty;
import lombok.extern.slf4j.Slf4j;
import org.assertj.core.util.Lists;
import org.springframework.stereotype.Service;

import java.util.List;

@Slf4j
@Service("orderService")
public class OrderService {

    @HystrixCommand(commandProperties = {
            @HystrixProperty(name = "execution.isolation.strategy", value = "THREAD"),
            @HystrixProperty(name = "execution.isolation.thread.timeoutInMilliseconds", value = "60000"),
            @HystrixProperty(name = "execution.isolation.thread.interruptOnTimeout", value = "true"),
            @HystrixProperty(name = "execution.isolation.thread.interruptOnCancel", value = "true"),
            @HystrixProperty(name = "execution.isolation.semaphore.maxConcurrentRequests", value = "100")})
    public OrderInfo getOrderInfo(String orderNo) {
        log.info("{}", orderNo);

        OrderInfo orderInfo = new OrderInfo();
        orderInfo.setOrderNo("6666666666");
        return orderInfo;
    }

    @HystrixCommand(threadPoolKey = "order", threadPoolProperties = {
            @HystrixProperty(name = "coreSize", value = "10"),
            @HystrixProperty(name = "allowMaximumSizeToDivergeFromCoreSize", value = "false"),
            @HystrixProperty(name = "maximumSize", value = "20"),
            @HystrixProperty(name = "maxQueueSize", value = "-1"),
            @HystrixProperty(name = "queueSizeRejectionThreshold", value = ""),
            @HystrixProperty(name = "keepAliveTimeMinutes", value = "")})
    public List<OrderInfo> getOrderLt() {
        log.info("");
        List<OrderInfo> orderInfoLt = Lists.newArrayList();
        return orderInfoLt;
    }
}
