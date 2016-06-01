package hello;

import com.google.common.util.concurrent.*;
import org.springframework.beans.factory.annotation.*;
import org.springframework.context.annotation.*;
import org.springframework.http.client.*;
import org.springframework.web.client.*;

import java.util.concurrent.*;

/**
 * Created by ameet.chaubal on 5/31/2016.
 */
@Configuration
public class AppConfig {

    @Autowired
    private ConfigVars configVars;

    /**
     * This {@link RestTemplate} allows us to inject a read and connect timeout for the
     * HTTP calls.
     *
     * @return
     */
    @Bean
    public RestTemplate restTemplate() {
        return new RestTemplate(clientHttpRequestFactory());
    }

    @Bean
    public AsyncRestTemplate asyncRestTemplate() {
        return new AsyncRestTemplate(asyncClientHttpRequestFactory());
    }

    /**
     * this bean enables us to terminate a connection after desired amount of time
     * It's a scheduler thread that is invoked after the set duration of time.
     *
     * @return
     */
    @Bean
    public ScheduledExecutorService scheduler() {
        return Executors.newScheduledThreadPool(
                1,
                new ThreadFactoryBuilder()
                        .setDaemon(true)
                        .setNameFormat("failAfter-%d")
                        .build());
    }

    @Bean
    public ListeningExecutorService listeningExecutorService() {
        return MoreExecutors.listeningDecorator(Executors.newFixedThreadPool(10, new ThreadFactoryBuilder()
                .setDaemon(true)
                .setNameFormat("guava-%d")
                .build()));
    }

    private ClientHttpRequestFactory clientHttpRequestFactory() {
        HttpComponentsClientHttpRequestFactory factory = new HttpComponentsClientHttpRequestFactory();
        factory.setReadTimeout(configVars.getTIMEOUT_MS());
        factory.setConnectTimeout(configVars.getTIMEOUT_MS());
        return factory;
    }

    private AsyncClientHttpRequestFactory asyncClientHttpRequestFactory() {
        HttpComponentsAsyncClientHttpRequestFactory factory = new HttpComponentsAsyncClientHttpRequestFactory();
        factory.setReadTimeout(configVars.getTIMEOUT_MS());
        factory.setConnectTimeout(configVars.getTIMEOUT_MS());
        return factory;
    }
}
