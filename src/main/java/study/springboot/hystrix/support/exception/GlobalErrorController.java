package study.springboot.hystrix.support.exception;

import com.google.common.collect.Maps;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.web.servlet.error.ErrorController;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Map;

/**
 * （1）还可以处理非Controller层抛出的异常，例如404 405
 * （2）如果没有配置ErrorController, SpringBoot会通过ErrorMvcAutoConfiguration自动配置一个，
 * 默认的实现类为 BasicErrorController。
 */
@Slf4j
//@RestController
public class GlobalErrorController implements ErrorController {

    private static final String ERROR_PATH = "redirect:/error";

    @Override
    public String getErrorPath() {
        return ERROR_PATH;
    }

    @RequestMapping(value = "/error", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public ResponseEntity errorData(HttpServletRequest request, HttpServletResponse response) {
        log.info("======> GlobalErrorController");
        String requestId = request.getHeader("Request-Id");
        String uri = request.getRequestURI();
        log.info(" i am ErrorController!===>{},{}", requestId, uri);

        int statusCode = ErrorUtils.getStatusCode(request);
        Exception exception = ErrorUtils.getException(request);
        String message = ErrorUtils.getMessage(request);
        String ur = (String) request.getAttribute("raw_url");

        Map<String, Object> data = Maps.newHashMap();
        if (500 == statusCode) {
            data.put("code", "9999");
            data.put("desc", "系统异常");
        } else if (404 == statusCode) {
            data.put("code", "9999");
            data.put("desc", "非法URL");
        }
        ResponseEntity entity = new ResponseEntity(data, HttpStatus.OK);
        return entity;
    }

    @RequestMapping(value = ERROR_PATH, produces = MediaType.TEXT_HTML_VALUE)
    public ModelAndView errorPage(HttpServletRequest request) {
        return new ModelAndView("globalError");
    }
//
//    private Map<String, Object> getErrorAttributes(HttpServletRequest request) {
//        RequestAttributes requestAttributes = new ServletRequestAttributes(request);
//        Map<String, Object> map = errorAttributes.getErrorAttributes(requestAttributes);
//        if (request.getAttribute("status") instanceof Integer) {
//            map.put("status", request.getAttribute("status"));
//        }
//        map.put("url", request.getRequestURL().toString());
//        map.putIfAbsent("path", request.getAttribute("raw_url"));
//        return map;
//    }

}
