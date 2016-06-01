package hello;

import com.sun.org.apache.xpath.internal.operations.*;
import model.*;
import org.slf4j.*;
import org.springframework.beans.factory.annotation.*;
import org.springframework.context.annotation.*;
import org.springframework.stereotype.*;

import java.time.*;
import java.util.*;
import java.util.concurrent.*;
import java.util.function.*;
import java.util.stream.*;

import static java.util.concurrent.TimeUnit.MILLISECONDS;
import static java.util.concurrent.TimeUnit.SECONDS;

/**
 * Created by ameet.chaubal on 5/31/2016.
 */
@Service
public class AsyncProcessor {
    private static final int TIMEOUT_POOL_SIZE = 5;
    private static final int TIMEOUT_SEC = 2;
    final static Logger LOGGER = LoggerFactory.getLogger(AsyncProcessor.class);

    @Autowired
    private ScheduledExecutorService scheduler;

    @Autowired
    QuoteLookupService quoteService;

    /**
     * Uses a concurrency method based on {@link Future} to perform
     * parallel operations. However, as can be seen here, the final 'materialization'
     * requires {@link Future#get()} call on each, which will block.
     *
     * @param i
     * @throws ExecutionException
     * @throws InterruptedException
     */
    public List<Quote> multiSimpleQuotes(int i) throws ExecutionException, InterruptedException {
        long s = System.currentTimeMillis();
        List<Future<Quote>> futures = new ArrayList<>();
        for (int k = 0; k < i; k++) {
            futures.add(quoteService.simpleFutureQuote());
        }
        List<Quote> quotes = new ArrayList<>();
        for (Future<Quote> f : futures) {
            Quote q = f.get();
            quotes.add(q);
        }
        System.out.println("Completed in:" + (System.currentTimeMillis() - s) + " ms.");
        return quotes;
    }

    /**
     * this tries to return a list
     *
     * @param i
     */
    public List<Quote> multiCompletableQuotesWithReturnList(int i) throws ExecutionException, InterruptedException {
        long s = System.currentTimeMillis();
        List<CompletableFuture<Quote>> futureResults = getCompletableFutureList(i);

        CompletableFuture[] futureArray = new CompletableFuture[futureResults.size()];
        futureArray = futureResults.toArray(futureArray);
        CompletableFuture.allOf(futureArray).join();

        List<Quote> quotes = new ArrayList<>();
        for (CompletableFuture f : futureArray) {
            quotes.add((Quote) f.get());
        }
        return quotes;
    }

    public List<Quote> multiQuotesWithTimeout(int i) throws ExecutionException, InterruptedException, TimeoutException {

        long s = System.currentTimeMillis();
        List<CompletableFuture<Quote>> futureResults = getCompletableFutureList(i);

        CompletableFuture[] futureArray = new CompletableFuture[futureResults.size()];
        futureArray = futureResults.toArray(futureArray);
        CompletableFuture.allOf(futureArray).join();

        List<Quote> quotes = new ArrayList<>();
        for (CompletableFuture f : futureArray) {
            quotes.add((Quote) f.get(TIMEOUT_SEC, SECONDS));
        }
        System.out.println("Completed with timeout in:" + (System.currentTimeMillis() - s) + " ms.");
        return quotes;
    }

    /**
     * based on the index, run respective calls and return the result
     *
     * @param i
     * @return
     */
    private List<CompletableFuture<Quote>> getCompletableFutureList(int i) {
        CompletableFuture<Quote> oneSecondTimeout = failAfter(Duration.ofSeconds(TIMEOUT_SEC));
        long s = System.currentTimeMillis();
        List<CompletableFuture<Quote>> futureResults = IntStream.rangeClosed(1, i).mapToObj
                (Integer -> quoteService
                        .getCompletableQuote().applyToEitherAsync(oneSecondTimeout, Function.identity())
                        .exceptionally(this::handleException)).
                collect(Collectors.toList());
        System.out.println("Future generation Completed in:" + (System.currentTimeMillis() - s) + " ms.");
        return futureResults;
    }

    private CompletableFuture<Quote> failAfter(Duration duration) {
        CompletableFuture<Quote> promise = new CompletableFuture<>();
        scheduler.schedule(() -> {
            final TimeoutException ex = new TimeoutException("Timeout after " + duration);
            return promise.completeExceptionally(ex);
        }, duration.toMillis(), MILLISECONDS);
        return promise;
    }

    private Quote handleException(Throwable throwable) {
        LOGGER.error(">>>Unrecoverable timeout error, {}", throwable);
        return null;
    }

    private void processWrapper(CompletableFuture<Quote> future) {
        future.thenRun(() -> System.out.println(future.join()));
    }
}
