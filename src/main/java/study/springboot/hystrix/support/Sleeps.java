package study.springboot.hystrix.support;

import java.util.concurrent.TimeUnit;

public class Sleeps {

    public static void seconds(long timeout) {
        try {
            TimeUnit.SECONDS.sleep(timeout);
        } catch (InterruptedException ex) {
            ex.printStackTrace();
        }
    }
}
