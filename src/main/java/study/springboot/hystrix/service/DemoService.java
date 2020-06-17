package study.springboot.hystrix.service;

import com.netflix.hystrix.contrib.javanica.annotation.HystrixCommand;
import com.netflix.hystrix.contrib.javanica.annotation.HystrixProperty;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

@Slf4j
@Service("demoService")
public class DemoService {

    @Autowired
    private RestTemplate restTemplate;

    @HystrixCommand(groupKey = "tst", commandKey = "ttsss",
            threadPoolKey = "test", threadPoolProperties = {
            @HystrixProperty(name = "coreSize", value = "30"),
            @HystrixProperty(name = "maxQueueSize", value = "101"),
            @HystrixProperty(name = "keepAliveTimeMinutes", value = "2"),
            @HystrixProperty(name = "queueSizeRejectionThreshold", value = "15")
    },
            fallbackMethod = "$sayHi", defaultFallback = "")
    public String sayHi() {
        log.info("fsadfasfasd");
        log.info("fsadfasfasd");
        log.info("fsadfasfasd");

        restTemplate.getForObject("", String.class);
        return "ok";
    }



    public String $sayHi(Throwable ex) {
        ex.printStackTrace();
        log.info("降级");
        return "error";
    }
}
