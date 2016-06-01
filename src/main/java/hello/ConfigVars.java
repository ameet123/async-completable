package hello;

import org.springframework.beans.factory.annotation.*;
import org.springframework.boot.context.properties.*;
import org.springframework.stereotype.*;

/**
 * Created by ameet.chaubal on 6/1/2016.
 */
@Component
public class ConfigVars {

    @Value("${app.rest_url}")
    private String APP_URL;

    @Value("${app.quote_count}")
    private int QUOTE_COUNT;

    @Value("${app.timeout_sec}")
    private int TIMEOUT_SEC;

    @Value("${app.timeout_ms}")
    private int TIMEOUT_MS;
    @Value("${app.intentional_delay_ms}")
    private int INTENTIONAL_DELAY_MS;

    public int getINTENTIONAL_DELAY_MS() {
        return INTENTIONAL_DELAY_MS;
    }

    public void setINTENTIONAL_DELAY_MS(int INTENTIONAL_DELAY_MS) {
        this.INTENTIONAL_DELAY_MS = INTENTIONAL_DELAY_MS;
    }

    public int getQUOTE_COUNT() {
        return QUOTE_COUNT;
    }

    public void setQUOTE_COUNT(int QUOTE_COUNT) {
        this.QUOTE_COUNT = QUOTE_COUNT;
    }

    public int getTIMEOUT_SEC() {
        return TIMEOUT_SEC;
    }

    public void setTIMEOUT_SEC(int TIMEOUT_SEC) {
        this.TIMEOUT_SEC = TIMEOUT_SEC;
    }

    public int getTIMEOUT_MS() {
        return TIMEOUT_MS;
    }

    public void setTIMEOUT_MS(int TIMEOUT_MS) {
        this.TIMEOUT_MS = TIMEOUT_MS;
    }

    public String getAPP_URL() {
        return APP_URL;
    }

    public void setAPP_URL(String APP_URL) {
        this.APP_URL = APP_URL;
    }
}
