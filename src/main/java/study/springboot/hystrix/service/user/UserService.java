package study.springboot.hystrix.service.user;

import com.netflix.hystrix.contrib.javanica.annotation.HystrixCommand;
import com.netflix.hystrix.contrib.javanica.command.AsyncResult;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import rx.Observable;
import rx.Single;

import java.util.concurrent.Future;

@Slf4j
@Service("userService")
public class UserService {

    /**
     * ====================
     * 同步执行
     * ====================
     */
    @HystrixCommand
    public User getUser(String userId) {
        log.info("fffffffffff");
        User user = new User();
        user.setUserId(900001L);
        user.setUserName("test1");
        return user;
    }

    /**
     * ====================
     * 异步执行
     * ====================
     */
    @HystrixCommand
    public Future<User> getUserByAsync(final String userId) {
        return new AsyncResult<User>() {
            @Override
            public User invoke() {
                log.info("fffffffffffffff");
                return new User();
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
