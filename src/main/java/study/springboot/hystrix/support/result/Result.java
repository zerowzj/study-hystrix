package study.springboot.hystrix.support.result;

import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class Result<T> {

    private String code;

    private String desc;

    private T data;

    Result() {
        this("0000", "成功");
    }

    Result(T data) {
        this();
        this.data = data;
    }

    Result(String code, String desc) {
        this.code = code;
        this.desc = desc;
    }
}
