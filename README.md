### Executor Framework:

Working with directly threads have two problems
Availability => Limited number of threads because if we are creating too many threads we will end up crashing the application
Cost => Creating too many threads will be costly

#### Thread Pools:  
Thread pool will have set of threads called as Worker Threads. This threads can be reused to execute several tasks. When a worker thread is finishes its work, it will return back to the pool. These threads are not destroyed and created. It will always available. If all the threads are buys the new task will wait in the Queue. Once the threads available it will start fetch the task from the Queue.

#### Executor Service:  
Thread Pool can be created by using the implementation factory methods of ExecutorService.
1) ThreadPoolExecutor => Typical thread pool task executor
2) ScheduledThreadPoolExecutor => we can schedule task to run at specific time or periodically.
3) ForkJoinPool => It will recursively breakdown big task into smaller task and execute them. (like divide and conquer)

We can create thread pools and once created we can pass the Runnable Task or Callable Task to the submit method to run the tasks.
```
ExecutorService executorService = Executors.newFixedThreadPool(2);
try {
	executorService.submit(() -> System.out.println(Thread.currentThread().getName()));
} finally {
	executorService.shutdown();
}
```

Once all the tasks are executed, ExecutorService will wait for the next tasks. So we need to shutdown ExecutorService properly. If we want to stop all the running task immediately we need to use shutdownNow() method or we can shutdown once all the running task are completed using shutdown() method.

ExecutorService will simplify the thread manipulation issues but Concurrency issues are still present.

##### Callable:  
It similar to Runnable task but it will return the result of the execution.

##### Future:  
It represent the future result of an operation either now or later point of time. So the result of the future object will not be calculated immediately. When we call future.get() the 
current thread will wait for the task result. 

```
private static void callableAndFutureDemo() {
	ExecutorService executorService = Executors.newFixedThreadPool(2);
	try {
		Future<Integer> future = executorService.submit(() -> {
			LongTask.simulateApiCall();
			return 1;
		});
		System.out.println("simulate call happening in new thread");
		Integer result = future.get();
		System.out.println("simulate call completed:"+result);
	} catch (InterruptedException | ExecutionException e) {
		e.printStackTrace();
	} finally {
		executorService.shutdown();
	}
}
```

#### CompletableFuture:  
CompletableFuture is a Future that may be explicitly completed (setting its value and status), and may be used as a CompletionStage, supporting dependent functions and actions that trigger upon its completion. Every CompletableFuture is future object. With this we can explicitly complete the future. 

We can call runAsync or supplyAsync method to execute a task. It will return CompletableFuture of void or CompletableFuture of return type. Both these methods accept ExecutorService as well. If we are not providing any it will take the threads from the
common pool and execute it. CompletableFuture implemented the methods from the interfaces Future and CompletionStage.

Using the CompletableFuture we can execute a task which will return CompletableFuture<Void> or will return CompletableFuture<AnyType>.

**1) CompletableFuture.runAsync:**  
This method will accept Runnable Interface and an optional Executor for running under a specific thread pool. If the Executor is provided it will use the common thread pool. This will return CompletableFuture<Void>.

```
private static void callRunAsync() {
	CompletableFuture<Void> future = CompletableFuture.runAsync(() -> System.out.println("Runnable Implementation"));
}
```

**2) CompletableFuture.supplyAsync:**  
This method will accept Supplier Interface and an optional Executor for running under a specific thread pool. If the Executor is provided it will use the common thread pool. This will return CompletableFuture<AnyType>.

```
private static void callSupplyAsync() {
	Supplier<Integer> task = () -> 1;
	CompletableFuture<Integer> future = CompletableFuture.supplyAsync(task);
	try {
		Integer result = future.get();
		System.out.println("Result:"+result);
	} catch (InterruptedException | ExecutionException e) {
		e.printStackTrace();
	}
}
```

As like in the ExecutorService Future.get, the CompletableFuture get method also a blocking operation. It will block the main thread from doing other operations until the result is ready. But CompletableFuture has provided other methods to handle this blocking operations.

#### Converting Synchronize Method to Async:  

Simple way to convert an existing Synchronized method into Async way.

```
public void sendMail() {
	System.out.println("Mail Initiated...");
	LongTask.simulate(3000);
	System.out.println("Mail Sent...");
}

public CompletableFuture sendMailAsyn() {
	return CompletableFuture.runAsync(() -> sendMail());
}
```

#### Completion:  
If we want do a operation after the completion of Aysnc method we can use the below ways.

####1) thenRun(Runnable):  
After the completion of the supplyAsync method, the future object will proceed for thenRun method, which will accept the Runnable method to execute. Point to note here is supplyAsync method is executed in new thread, if this thread doesn't have any delay then the future.thenRun will be executed by the main thread itself. If there is a delay(LongTask.simulate) thenRun method will be executed by new available thread.

```
private static void thenRunBehavior() {
	CompletableFuture future = CompletableFuture.supplyAsync(() -> {
		LongTask.simulate(2000);
		System.out.println("Async call is processed by:"+Thread.currentThread().getName()); //ForkJoinPool.commonPool-worker-1
		return 1;
	});
	future.thenRun(()-> {
		System.out.println("Successfully completed Async call in:"+Thread.currentThread().getName()); // ForkJoinPool.commonPool-worker-1
	});
	System.out.println("Waiting for the result");
	LongTask.simulate(3000);
}
```
**In the case of delay the result:**  
Waiting for the result  
Async call is processed by:ForkJoinPool.commonPool-worker-1  
Successfully completed Async call in:ForkJoinPool.commonPool-worker-1  

**In the case of no delay the result:(commenting out LongTask.simulate(2000))**  
Async call is processed by:ForkJoinPool.commonPool-worker-1  
Successfully completed Async call in:main  
Waiting for the result  

#### 2) thenRunAsync(Runnable):  
thenRunAsync will always be executed in a new thread even though the caller thread is available after the CompletableFuture has completed.

```
private static void thenRunAsyncBehavior() {
	CompletableFuture future = CompletableFuture.supplyAsync(() -> {
		LongTask.simulate(2000);
		System.out.println("Async call is processed by:"+Thread.currentThread().getName());
		return 1;
	});
	future.thenRunAsync(()-> {
		System.out.println("Successfully completed Async call in:"+Thread.currentThread().getName());
	});
	System.out.println("Waiting for the result");
	LongTask.simulate(3000);
}
```
**In the case of delay the result:**  
Waiting for the result  
Async call is processed by:ForkJoinPool.commonPool-worker-1  
Successfully completed Async call in:ForkJoinPool.commonPool-worker-1  

**In the case of no delay the result:(commenting out LongTask.simulate(2000))**  
Async call is processed by:ForkJoinPool.commonPool-worker-1  
Waiting for the result  
Successfully completed Async call in:ForkJoinPool.commonPool-worker-1  

**Difference:**  
The method thenRun allows the execution of the Runnable directly in the caller’s thread, if the CompletableFuture has already completed. Since even in a direct invocation chain like CompletableFuture.runAsync(…).thenRun(…); there’s the possibility that the asynchronous task has already completed by the time thenRun is invoked, there’s the possibility that the dependent action is executed in the caller’s thread, unlike thenRunAsync which will always use the default (or provided) executor.

#### Parse the CompletableFuture result:  
If we want to process the result of the CompletableFuture we can use the below methods. Difference between these two methods are similar like thenRun and thenRunAsync.  
#### 1) thenAccept(Consumer):  
```
private static void thenAcceptBehavior() {
	CompletableFuture future = CompletableFuture.supplyAsync(() -> {
		LongTask.simulate(2000);
		System.out.println("Async call is processed by:"+Thread.currentThread().getName());
		return 1;
	});
	future.thenAccept(result-> {
		System.out.println("Successfully completed Async call in:"+Thread.currentThread().getName());
		System.out.println("result:"+result);

	});
	System.out.println("Waiting for the result");
	LongTask.simulate(3000);
}
```
**In the case of delay the result:**  
Waiting for the result  
Async call is processed by:ForkJoinPool.commonPool-worker-1  
Successfully completed Async call in:ForkJoinPool.commonPool-worker-1  
result:1  

**In the case of no delay the result:(commenting out LongTask.simulate(2000))**  
Async call is processed by:ForkJoinPool.commonPool-worker-1  
Successfully completed Async call in:main  
result:1  
Waiting for the result  

#### 2) thenAcceptAsync(Consumer):  

```
private static void tthenAcceptAsyncBehavior() {
	CompletableFuture future = CompletableFuture.supplyAsync(() -> {
		LongTask.simulate(2000);
		System.out.println("Async call is processed by:"+Thread.currentThread().getName());
		return 1;
	});
	future.thenAcceptAsync(result-> {
		System.out.println("Successfully completed Async call in:"+Thread.currentThread().getName());
		System.out.println("result:"+result);

	});
	System.out.println("Waiting for the result");
	LongTask.simulate(3000);
}
```
**In the case of delay the result:**  
Waiting for the result  
Async call is processed by:ForkJoinPool.commonPool-worker-1  
Successfully completed Async call in:ForkJoinPool.commonPool-worker-1  
result:1  

**In the case of no delay the result:(commenting out LongTask.simulate(2000))**  
Async call is processed by:ForkJoinPool.commonPool-worker-1  
Waiting for the result  
Successfully completed Async call in:ForkJoinPool.commonPool-worker-1  
result:1  

#### Handling Exceptions:  
In Future interface there is no functionality to handle the exceptions. But in CompletableFuture we have special handling where it will wrap the actual exception caused inside the 
Aysnc task as the ExecutionException. Using the future.get() method we can get the ExecutionException.

```
public static void exceptionDemo() {
	CompletableFuture future = CompletableFuture.supplyAsync(() -> {
		System.out.println("Calling weather service:" + Thread.currentThread().getName());
		throw new IllegalStateException();
	});
	try {
		future.get();
	} catch (InterruptedException e) {
		e.printStackTrace();
	} catch (ExecutionException e) {
		e.printStackTrace();
	}
}
```

We can also have exceptionally method which accept the Throwable Function. This will give us the flexibility to give the custom or default response. This exceptionally method will also return new CompletableFuture, in which we can use get() method to get the value.

```
public static void exceptionWithoutBreaking() {
	CompletableFuture future = CompletableFuture.supplyAsync(() -> {
		System.out.println("Calling weather service:" + Thread.currentThread().getName());
		throw new IllegalStateException();
	});
	try {
		Object result = future.exceptionally(ex -> {
			System.out.println("The error is occurred, so we will send the default response");
			return 1;
		}).get();
		System.out.println("result:"+result);
	} catch (InterruptedException e) {
		e.printStackTrace();
	} catch (ExecutionException e) {
		e.printStackTrace();
	}
}
```

#### Transforming Results:thenApply()  
We can use thenApply / thenApplyAsync method to process the result of the Future object example here we convert temperature from Celsius to Fahrenheit. Once the Future object is processed we can use the thenAccept method to get the result and print it.

```
 public static void transformFuture() {
	CompletableFuture<Integer> future = CompletableFuture.supplyAsync(() -> 30);
	future
		.thenApply(CompletableFutureTransformDemo::toFahrenheit)
		.thenAccept(System.out::println);
}

private static double toFahrenheit(double celsius) {
	return (celsius * 1.8) + 32;
}

```

#### Composing CompletableFuture:thenCompose()  
thenCompose() is used to combine two CompletableFutures where one future is dependent on the other.
We can use thenCompose / thenComposeAsync to call the second Async task upon completion of the first task. In this we can use these method to chain together all the CompletableFutures.
thenCompose / thenComposeAsync will return CompletionStage, which means it will give new CompletableFuture. so we can chain together.

```
public static void composeFutures() {
	//id-> fetch email from DB using the user id
	//with email fetch the playlist of the user from online stream application
	getUserEmailAsync()
			.thenCompose(email -> getPlaylistAsync(email))
			.thenAccept(System.out::println);
}

private static CompletableFuture<String> getUserEmailAsync() {
	System.out.println("Retrieving Email from DB");
	return CompletableFuture.supplyAsync(() -> "email");
}

private static CompletableFuture<String> getPlaylistAsync(String email) {
	System.out.println("Fetching playlist from streaming website for the email");
	return CompletableFuture.supplyAsync(() -> "playlist1");
}
```

#### Combining CompletableFuture:thenCombine()  
thenCombine() is used when we want two Futures to run independently and do something after both are complete. The callback function passed to thenCombine() will be called when both the CompletableFutures are complete.
We can start two Async task in parallel and once the results are ready we can combine it together. For example we are getting the price of an item as well as the currency conversion rate. Once the results are ready we can combine them together. For this we can use thenCombine / thenCombineAsync. 

```
public static void combineFutures() {
	//Getting item price
	CompletableFuture<Integer> firstTask = CompletableFuture.supplyAsync(() -> 20);

	//Getting conversion rate
	CompletableFuture<Double> secondTask = CompletableFuture.supplyAsync(() -> 0.9);

	firstTask
			.thenCombine(secondTask, (a, b) -> a*b)
			.thenAccept(System.out::println);

}
```

#### Waiting for Task Completion:allOf & anyOf  
**allOf:**  
We can use allOf method to wait for all the task to be completed. It will return CompletableFuture of Void. 
```
public static void waitForAllTaskCompletion() {
	CompletableFuture<Integer> first = CompletableFuture.supplyAsync(() -> 1);
	CompletableFuture<Integer> second = CompletableFuture.supplyAsync(() -> 2);
	CompletableFuture<Integer> third = CompletableFuture.supplyAsync(() -> 3);

	CompletableFuture<Void> all = CompletableFuture.allOf(first, second, third);
	all.thenRun(() -> {
		try {
			Integer firstResult = first.get();
			System.out.println("first result:"+firstResult);
		} catch (InterruptedException e) {
			e.printStackTrace();
		} catch (ExecutionException e) {
			e.printStackTrace();
		}
		System.out.println("All Tasks are completed");
	});
}
```
Here first.get() will not block the main thread because its running in the new thread.

**anyOf:**  
We can use anyOf method to get the same result from the different services( like slower and faster). Which ever gives the result we can return it immediately.

```
public static void waitForAnyoneTaskCompleted() {
	CompletableFuture<Integer> slowService =
			CompletableFuture.supplyAsync(() -> {
				LongTask.simulate(2000);
				return 20;
			});
	CompletableFuture<Integer> fastService = CompletableFuture.supplyAsync(() -> 20);

	CompletableFuture.anyOf(slowService, fastService)
			.thenAccept(result -> System.out.println(result));
}
```

As soon as the result arrived from anyone service, it will be returned.

#### Handling Timeouts:orTimoeut  
We can specify explicit timeout for the future response which will take more time in consuming the external/remote service. These methods are introduced in the Jdk9

```
public static void timeoutExceptionDemo() {
	CompletableFuture<Integer> future = CompletableFuture.supplyAsync(() -> {
		System.out.println("Calling a slow service which will response after 3 seconds");
		LongTask.simulate(3000);
		return 10;
	});

	try {
		future
				.orTimeout(1, TimeUnit.SECONDS)
				.get();
	} catch (InterruptedException e) {
		e.printStackTrace();
	} catch (ExecutionException e) {
		e.printStackTrace();
	}
}
```
future.orTimeout(1, TimeUnit.SECONDS) will timeout after 1 seconds and wrap the TimeoutException inside the ExecutionException.

**completeOnTimeout:**  
Instead simply catching exception we can use the completeOnTimeout method to give back the default value instead of throwing an TimeoutException.

```
public static void timeoutExceptionDefaultValueDemo() {
	CompletableFuture<Integer> future = CompletableFuture.supplyAsync(() -> {
		System.out.println("Calling a slow service which will response after 3 seconds");
		LongTask.simulate(3000);
		return 10;
	});

	future
			.completeOnTimeout(20, 1, TimeUnit.SECONDS)
			.thenAccept(System.out::println);
	LongTask.simulate(3000);
}

```

##### FlightBookingDemo.java
Finally implemented a Flight Price Quoting Service where have to fetch the prices from three different sites simultaneously and print them once the results arrived.


