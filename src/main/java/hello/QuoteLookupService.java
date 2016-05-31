package hello;

import model.*;
import org.springframework.scheduling.annotation.*;
import org.springframework.stereotype.*;
import org.springframework.web.client.*;

import java.util.*;
import java.util.concurrent.*;
import java.util.stream.*;

import static java.util.stream.Collectors.toList;

/**
 * Created by ameet.chaubal on 5/31/2016.
 */
@Service
public class QuoteLookupService {

    RestTemplate restTemplate = new RestTemplate();

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
        return new AsyncResult<Quote>(quoteResource.getValue());
    }

    private String getThreadName() {
        return Thread.currentThread().getName();
    }

    private QuoteResource fetchQuote() {
        QuoteResource quoteResource =
                restTemplate.getForObject("http://gturnquist-quoters.cfapps.io/api/random", QuoteResource.class);
        quoteResource.getValue().setThreadName(getThreadName());
        // intentional delay
        try {
            Thread.sleep(1000L);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        return quoteResource;
    }
}
