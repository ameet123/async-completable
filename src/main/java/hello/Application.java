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
    private static final int NUMBER_OF_QUOTES = 5;

    @Autowired
    private QuoteLookupService quoteService;
    @Autowired
    private AsyncProcessor asyncProcessor;

    @Override
    public void run(String... args) throws Exception {
        // Start the clock
        long start = System.currentTimeMillis();
        List<Quote> quotes;
        // approach 1
        quotes = asyncProcessor.multiSimpleQuotes(NUMBER_OF_QUOTES);

        // approach 2
//        asyncProcessor.multiCompletableQuotes(NUMBER_OF_QUOTES);

        // approach 3
//        quotes = asyncProcessor.multiCompletableQuotesWithReturnList(NUMBER_OF_QUOTES);

        // approach 4
        quotes = asyncProcessor.multiQuotesWithTimeout(NUMBER_OF_QUOTES);

        // final print
        quotes.stream().forEach(q -> System.out.println(q));
    }


    public static void main(String[] args) {
        SpringApplication.run(Application.class, args);
    }
}
