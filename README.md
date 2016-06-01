# async-completable
completable futures with spring

This project demonstrates different ways in which asynchronous processing can be performed.

It's a spring boot project which demonstrates multiple approaches.

###1.Simple Future### 
This approach simply uses Spring `Async` annotation and returns java Futures.

***Con***: On receiving the Futures, when a get() call is made, the thread will block.
This will essentially block processing for the other threads.

***Pros***: Simple and well-known

###2.Completable Future###
This approach uses Java 8 `CompletableFuture` which enables us to combine the futures from all the async
calls and then join them without blocking, returning when all is completed.
***Cons***: A bit involved

***Pros***: non-blocking and more event driven

###3.Completable Future with Timeout and exception handling###
This adds timeout to previous approach
***Cons***: more involved

***Pros***: takes care of timeouts

##References##
1. http://www.nurkiewicz.com/2014/12/asynchronous-timeouts-with.html
2. http://www.nurkiewicz.com/2013/05/java-8-definitive-guide-to.html 
