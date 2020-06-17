package study.springboot.hystrix.service;

import com.netflix.hystrix.contrib.javanica.annotation.HystrixCommand;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

@Slf4j
@Service("demoService")
public class DemoService {

    @Autowired
    private RestTemplate restTemplate;

    @HystrixCommand(groupKey = "", commandKey = "",
            fallbackMethod = "$sayHi", defaultFallback = "")
    public String sayHi() {
        restTemplate.getForObject("", String.class);
        return "ok";
    }

    public String $sayHi(Throwable ex) {
        ex.printStackTrace();
        log.info("降级");
        return "error";
    }
}
