package study.springboot.hystrix.support.result;

import com.google.common.collect.Maps;

import java.util.Map;

public class Results {

    public static Map<String, Object> data() {
        return Maps.newHashMap();
    }

    public static Result success() {
        return new Result();
    }

    public static <T> Result<T> success(T data) {
        return new Result(data);
    }
}
