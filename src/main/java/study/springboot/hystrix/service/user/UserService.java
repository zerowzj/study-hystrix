package study.springboot.hystrix.service.user;

import com.netflix.hystrix.contrib.javanica.annotation.HystrixCommand;
import com.netflix.hystrix.contrib.javanica.annotation.HystrixProperty;
import com.netflix.hystrix.contrib.javanica.command.AsyncResult;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;

@Slf4j
@Service("userService")
public class UserService {

    /**
     * ====================
     * 同步执行
     * ====================
     */
    @HystrixCommand(fallbackMethod = "$getUserInfo", commandProperties = {
            @HystrixProperty(name = "execution.isolation.thread.timeoutInMilliseconds", value = "5000")}
    )
    public UserInfo getUser(Long userId) {
        log.info("fffffffffff");
        try {
            TimeUnit.SECONDS.sleep(10);
        } catch (InterruptedException ex) {
            ex.printStackTrace();
        }

        UserInfo user = new UserInfo();
        user.setUserId(900001L);
        user.setUserName("test1");
        return user;
    }

    public UserInfo $getUserInfo(Long userId, Throwable cause) {
        return null;
    }

    /**
     * ====================
     * 异步执行
     * ====================
     */
    @HystrixCommand
    public Future<UserInfo> getUserByAsync(final String userId) {
        return new AsyncResult<UserInfo>() {
            @Override
            public UserInfo invoke() {
                log.info("fffffffffffffff");
                return new UserInfo();
            }
        };
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
