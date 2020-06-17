package study.springboot.hystrix.support.exception;

import com.google.common.collect.Maps;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.Map;

/**
 * （1）ErrorController可对全局错误进行处理，但获取不到异常的具体信息，同时也无法根据异常类型进行不同的响应，例如对自定义异常的处理
 * （2）@ControllerAdvice可对全局异常进行捕获，包括自定义异常，其是应用于对springmvc中的控制器抛出的异常进行处理
 * （3）404这样不会进入控制器处理的异常不起作用，此时还是要依靠ErrorController来处理
 */
// 可以配置拦截指定的类或者包等
// @RestControllerAdvice 使 @ExceptionHandler 注解的方法默认具有 @ResponseBody 注解
@Slf4j
//@EnableWebMvc
//@ControllerAdvice
@RestControllerAdvice
public class GlobalExceptionHandler {

    //配置拦截的错误类型
    //这里也可以返回 ModelAndView 导向错误视图
    @ExceptionHandler(Throwable.class)
    public Map<String, Object> resolveException(Exception ex) {
        ex.printStackTrace();
        log.info("======> GlobalExceptionHandler");
        Map<String, Object> data = Maps.newHashMap();
        data.put("code", "9999");
        data.put("desc", ex.getMessage());
        return data;
    }
}
