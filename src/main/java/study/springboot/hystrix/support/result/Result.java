package study.springboot.hystrix.support.result;

import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class Result<T> {

    private String code;

    private String desc;

    private T data;

    public Result() {
        this.code = "0000";
        this.desc = "成功";
    }

    public Result(T data) {
        this();
        this.data = data;
    }
}
