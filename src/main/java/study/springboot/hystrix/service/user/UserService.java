package study.springboot.hystrix.service.user;

import com.netflix.hystrix.contrib.javanica.annotation.DefaultProperties;
import com.netflix.hystrix.contrib.javanica.annotation.HystrixCommand;
import com.netflix.hystrix.contrib.javanica.annotation.HystrixProperty;
import com.netflix.hystrix.contrib.javanica.command.AsyncResult;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;

@Slf4j
@Service("userService")
@DefaultProperties
public class UserService {

    /**
     * ====================
     * 同步执行
     * ====================
     */
    @HystrixCommand(fallbackMethod = "$getUserInfo",
            commandProperties = {@HystrixProperty(name = "execution.timeout.enabled", value = "true"),
                    @HystrixProperty(name = "execution.isolation.thread.timeoutInMilliseconds", value = "5000")}
    )
    public UserInfo getUser(Long userId) {
        log.info("fffffffffff");
        try {
            TimeUnit.SECONDS.sleep(10);
        } catch (InterruptedException ex) {
            // ex.printStackTrace();
        }

        UserInfo user = new UserInfo();
        user.setUserId(900001L);
        user.setUserName("test1");
        return user;
    }

    /**
     * （1）fallback应该和注解方法在同一类下
     * （2）fallback的返回值和参数列表应该和注解方法一致，如果需要异常，则在末尾添加Throwable参数
     * （3）fallback方法上可以继续添加fallback
     */
    public UserInfo $getUserInfo(Long userId, Throwable ex) {
        log.error("fasdfasdfasd", ex);
        return null;
    }

    /**
     * ====================
     * 异步执行
     * ====================
     */
    @HystrixCommand
    public Future<UserInfo> getUserByAsync(final String userId) {
        Future<UserInfo> future = new AsyncResult<UserInfo>() {
            @Override
            public UserInfo invoke() {
                log.info("fffffffffffffff");
                try {
                    TimeUnit.SECONDS.sleep(5);
                } catch (Exception ex) {
                }
                return new UserInfo();
            }
        };
        return future;
    }

    /**
     * ====================
     * 反应执行
     * ====================
     */
//    @HystrixCommand
//    public Observable<String> getUserByRx(final String id) {
//        return Observable.create(new Single.OnSubscribe(Subscriber < ? > observer) ->{
//            observer.onNext(rpcPassportService.getUserById(id));
//        });
//    }
}
