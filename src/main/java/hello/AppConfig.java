package hello;

import com.google.common.util.concurrent.*;
import org.springframework.context.annotation.*;
import org.springframework.http.client.*;
import org.springframework.web.client.*;

import java.util.concurrent.*;

/**
 * Created by ameet.chaubal on 5/31/2016.
 */
@Configuration
public class AppConfig {

    private static final int REQ_TIMEOUT_MS = 2000;

    @Bean
    public RestTemplate restTemplate() {
        return new RestTemplate(clientHttpRequestFactory());
    }

    @Bean
    public ScheduledExecutorService scheduler() {
        return Executors.newScheduledThreadPool(
                1,
                new ThreadFactoryBuilder()
                        .setDaemon(true)
                        .setNameFormat("failAfter-%d")
                        .build());
    }

    private ClientHttpRequestFactory clientHttpRequestFactory() {
        HttpComponentsClientHttpRequestFactory factory = new HttpComponentsClientHttpRequestFactory();
        factory.setReadTimeout(REQ_TIMEOUT_MS);
        factory.setConnectTimeout(REQ_TIMEOUT_MS);
        return factory;
    }
}
