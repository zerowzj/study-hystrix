package study.springboot.hystrix.service;

import com.netflix.hystrix.contrib.javanica.annotation.HystrixCommand;
import com.netflix.hystrix.contrib.javanica.annotation.HystrixProperty;
import com.netflix.hystrix.contrib.javanica.command.AsyncResult;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import study.springboot.hystrix.support.Sleeps;
import study.springboot.hystrix.support.result.Results;

import java.util.Map;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;

@Slf4j
@Service("commandService")
public class CommandService {

    public Map<String, Object> get(String name) {
        log.info(">>>>>> {}", name);
        Sleeps.seconds(3);
        return Results.data();
    }

    /**
     * ====================
     * （★）同步执行
     * ====================
     */
    @HystrixCommand(commandProperties = {
            @HystrixProperty(name = "execution.timeout.enabled", value = "true"),
            @HystrixProperty(name = "fallback.enabled", value = "true")})
    public Map<String, Object> getBySync(String name) {
        log.info(">>>>>> {}", name);
        return Results.data();
    }

    /**
     * （1）fallback应该和注解方法在同一类下
     * （2）fallback的返回值和参数列表应该和注解方法一致，如果需要异常，则在末尾添加Throwable参数
     * （3）fallback方法上可以继续添加fallback
     */
    public Map<String, Object> $get(String name, Throwable ex) {
        log.error("fasdfasdfasd", ex);
        return null;
    }

    /**
     * ====================
     * （★）异步执行
     * ====================
     */
    public void getByAsync(Long timeout) {
        try {
            doByAsync(5L).get();
            doByAsync(8L).get();
        } catch (InterruptedException | ExecutionException ex) {

        }
    }

    @HystrixCommand
    public Future<Map<String, Object>> doByAsync(Long timeout) {
        log.info("ffffffffffffffff");
        Future<Map<String, Object>> future = new AsyncResult<Map<String, Object>>() {
            @Override
            public Map<String, Object> invoke() {
                log.info("fffffffffffffff");
                Sleeps.seconds(timeout);
                return null;
            }

            @Override
            public Map<String, Object> get() throws UnsupportedOperationException {
                return invoke();
            }
        };
        return future;
    }


    /**
     * ====================
     * （★）反应执行
     * ====================
     */
//    @HystrixCommand
//    public Observable<String> getUserByRx(final String id) {
//        return Observable.create(new Single.OnSubscribe(Subscriber < ? > observer) ->{
//            observer.onNext(rpcPassportService.getUserById(id));
//        });
//    }
}
