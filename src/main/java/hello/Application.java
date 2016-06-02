package hello;

import model.*;
import org.springframework.beans.factory.annotation.*;
import org.springframework.boot.*;
import org.springframework.boot.autoconfigure.*;
import org.springframework.scheduling.annotation.*;

import java.util.*;

/**
 * Created by ameet.chaubal on 5/31/2016.
 */
@SpringBootApplication
@EnableAsync
public class Application implements CommandLineRunner {

    @Autowired
    private QuoteLookupService quoteService;
    @Autowired
    private AsyncProcessor asyncProcessor;
    @Autowired
    private ConfigVars configVars;

    public static void main(String[] args) {
        SpringApplication.run(Application.class, args);
    }

    @Override
    public void run(String... args) throws Exception {
        // Start the clock
        long start = System.currentTimeMillis();
        List<Quote> quotes;

        // approach 1 ==> Just for DEMO , simple Future - not the best
//        quotes = asyncProcessor.multiSimpleQuotes(NUMBER_OF_QUOTES);

        // approach 2 ==> CompletableFuture, elegant but a tad bit involved to 'read'
//        quotes = asyncProcessor.multiQuotesWithTimeout(configVars.getQUOTE_COUNT());

        // approach 3 ==> Guava ListenableFuture, easy to read and extend
        quotes = asyncProcessor.multiQuoteGuava(configVars.getQUOTE_COUNT());
        System.out.println("Returned from multi guava... Items:" + quotes.size());

        // final print
        quotes.stream().forEach(System.out::println);
    }
}
