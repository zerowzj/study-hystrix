package study.springboot.hystrix.service.user;

import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class UserInfo {

    private Long userId;

    private String userName;
}