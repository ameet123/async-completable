package hello;

import com.google.common.util.concurrent.*;
import com.google.common.util.concurrent.ListenableFuture;
import model.*;
import org.slf4j.*;
import org.springframework.beans.factory.annotation.*;
import org.springframework.scheduling.annotation.*;
import org.springframework.stereotype.Service;

import org.springframework.web.client.*;

import java.util.*;
import java.util.concurrent.*;

/**
 * Created by ameet.chaubal on 5/31/2016.
 */
@Service
public class QuoteLookupService {
    final static Logger LOGGER = LoggerFactory.getLogger(AsyncProcessor.class);
    @Autowired
    private ConfigVars configVars;
    @Autowired
    RestTemplate restTemplate;
    @Autowired
    AsyncRestTemplate asyncRestTemplate;

    /**
     * get the quote using restTemplate and deserialize it to the object
     * This method is run in parallel
     */
    @Async
    public CompletableFuture<Quote> getCompletableQuote() {
        QuoteResource quoteResource = fetchQuote();
        return CompletableFuture.completedFuture(quoteResource.getValue());
    }

    /**
     * this is the older {@link Future} based async call
     *
     * @return
     */
    @Async
    public Future<Quote> simpleFutureQuote() {
        QuoteResource quoteResource = fetchQuote();
        return new AsyncResult<>(quoteResource.getValue());
    }

    private String getThreadName() {
        return Thread.currentThread().getName();
    }

    private QuoteResource fetchQuote() {
        QuoteResource quoteResource =
                restTemplate.getForObject(configVars.getAPP_URL(), QuoteResource.class);
        quoteResource.getValue().setThreadName(getThreadName());
        // intentional delay
        try {
            Thread.sleep(configVars.getINTENTIONAL_DELAY_MS());
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        return quoteResource;
    }
}
