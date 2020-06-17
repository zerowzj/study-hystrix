package study.springboot.hystrix.support.exception;

import javax.servlet.http.HttpServletRequest;

public class ErrorUtils {

    private static final String STATUS_CODE_KEY = "javax.servlet.error.status_code";

    private static final String EXCEPTION_KEY = "javax.servlet.error.exception";

    private static final String MESSAGE_KEY = "javax.servlet.error.message";

    public static int getStatusCode(HttpServletRequest request) {
        int statusCode = (int) request.getAttribute(STATUS_CODE_KEY);
        return statusCode;
    }

    public static Exception getException(HttpServletRequest request) {
        Exception ex = (Exception) request.getAttribute(EXCEPTION_KEY);
        return ex;
    }

    public static String getMessage(HttpServletRequest request) {
        String msg = (String) request.getAttribute(MESSAGE_KEY);
        return msg;
    }
}
