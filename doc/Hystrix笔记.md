# 背景





## Hystrix容错

Hystrix的容错主要是通过添加容许延迟和容错方法，帮助控制这些分布式服务之间的交互。 还通过隔离服务之间的访问点，阻止它们之间的级联故障以及提供回退选项来实现这一点，从而提高系统的整体弹性。Hystrix主要提供了以下几种容错方法：

- 资源隔离
- 熔断
- 降级



# Hystrix

​		Hystrix，中文含义是豪猪，因其背上长满棘刺，从而拥有了自我保护的能力。我们所说的Hystrix是Netflix开源的一款容错框架，同样具有自我保护能力。为了实现容错和自我保护，下面我们看看Hystrix如何设计和实现的。

Hystrix设计目标

- 对来自依赖的延迟和故障进行防护和控制——这些依赖通常都是通过网络访问的
- 阻止故障的连锁反应
- 快速失败并迅速恢复
- 回退并优雅降级
- 提供近实时的监控与告警

Hystrix遵循的设计原则

- 防止任何单独的依赖耗尽资源（线程）
- 过载立即切断并快速失败，防止排队
- 尽可能提供回退以保护用户免受故障
- 使用隔离技术（例如隔板，泳道和断路器模式）来限制任何一个依赖的影响
- 通过近实时的指标，监控和告警，确保故障被及时发现
- 通过动态修改配置属性，确保故障及时恢复
- 防止整个依赖客户端执行失败，而不仅仅是网络通信

Hystrix如何实现这些设计目标？

- 使用命令模式将所有对外部服务（或依赖关系）的调用包装在HystrixCommand或HystrixObservableCommand对象中，并将该对象放在单独的线程中执行；
- 每个依赖都维护着一个线程池（或信号量），线程池被耗尽则拒绝请求（而不是让请求排队）。
- 记录请求成功，失败，超时和线程拒绝。
- 服务错误百分比超过了阈值，熔断器开关自动打开，一段时间内停止对该服务的所有请求。
- 请求失败，被拒绝，超时或熔断时执行降级逻辑。
- 近实时地监控指标和配置的修改。



## Hystrix属性的4中优先级

1. 内置全局默认值（Global default from code）

   如果下面3种都没有设置，默认是使用此种，后面用“默认值”代指这种。

2. 动态全局默认属性（Dynamic global default property）

   可以通过属性配置来更改全局默认值，后面用“默认属性”代指这种。

3. 内置实例默认值（Instance default from code）

   在代码中，设置的属性值，后面用“”实例默认”来代指这种。

4. 动态配置实例属性（Dynamic instance property）

   可以针对特定的实例，动态配置属性值，来代替前面三种，后面用“实例属性”来代指这种。

> 注意：优先级：1 < 2 < 3 < 4

## Hystrix三种Command

- 同步执行

  当执行到注解方法时，程序会顺序执行。

- 异步执行

  当执行到注解方法时，会并发异步执行，返回一个Future对象，后面使用.get()方法来阻塞拿到结果。如果有多个方法时，执行时间就是其中最长的一个服务的执行时间。

- 反应执行

  当执行到注解方法时，返回一个观察者。支持EAGER和LAZY模式。和同步异步执行的区别是，当对多个方法之间的返回结果不需要做合并而是希望当多个方法返回时触发一些事件时比较适合使用该模式。



# 1. Hystrix 配置参数解析

## 1.1 HystrixCommand

### 配置方式

​		我们的配置都是基于 HystrixCommand 的，我们通过在方法上添加 `@HystrixCommand` 注解并配置注解的参数来实现配置，但有的时候一个类里面会有多个 Hystrix 方法，每个方法都是类似配置的话会冗余很多代码，这时候我们可以在类上使用 `@DefaultProperties` 注解来给整个类的 Hystrix 方法设置一个默认值。

### 配置项

​	下面是 HystrixCommand 支持的参数，除了 commandKey/observableExecutionMode/fallbackMethod 外，都可以使用 @DefaultProperties 配置默认值。

- commandKey：用来标识一个 Hystrix 命令，默认会取被注解的方法名。需要注意：Hystrix 里同一个键的唯一标识并不包括 groupKey，建议取一个独一二无的名字，防止多个方法之间因为键重复而互相影响。
- groupKey：一组 Hystrix 命令的集合， 用来统计、报告，默认取类名，可不配置。
- threadPoolKey：用来标识一个线程池，如果没设置的话会取 groupKey，很多情况下都是同一个类内的方法在共用同一个线程池，如果两个共用同一线程池的方法上配置了同样的属性，在第一个方法被执行后线程池的属性就固定了，所以属性会以第一个被执行的方法上的配置为准。
- commandProperties：与此命令相关的属性
- threadPoolProperties：与线程池相关的属性
- observableExecutionMode：当 Hystrix 命令被包装成 RxJava 的 Observer 异步执行时，此配置指定了 Observable 被执行的模式，默认是 ObservableExecutionMode.EAGER，Observable 会在被创建后立刻执行，而 ObservableExecutionMode.EAGER模式下，则会产生一个 Observable 被 subscribe 后执行。我们常见的命令都是同步执行的，此配置项可以不配置。
- ignoreExceptions：默认 Hystrix 在执行方法时捕获到异常时执行回退，并统计失败率以修改熔断器的状态，而被忽略的异常则会直接抛到外层，不会执行回退方法，也不会影响熔断器的状态。
- raiseHystrixExceptions：当配置项包括 HystrixRuntimeException 时，所有的未被忽略的异常都会被包装成 HystrixRuntimeException，配置其他种类的异常好像并没有什么影响。
- fallbackMethod：方法执行时熔断、错误、超时时会执行的回退方法，需要保持此方法与 Hystrix 方法的签名和返回值一致。
- defaultFallback：默认回退方法，当配置 fallbackMethod 项时此项没有意义，另外，默认回退方法不能有参数，返回值要与 Hystrix方法的返回值相同。

## 1.2 commandProperties

### 配置方式

​		Hystrix 的命令属性是由 @HystrixProperty 注解数组构成的，HystrixProperty 由 name 和 value 两个属性，数据类型都是字符串。

### 配置项

#### （1）执行策略

- execution.isolation.strategy：配置请求隔离的方式，有 threadPool（线程池，默认）和 semaphore（信号量）两种，信号量方式高效但配置不灵活，我们一般采用 Java 里常用的线程池方式。
- execution.timeout.enabled：是否给方法执行设置超时，默认为 true。
- execution.isolation.thread.timeoutInMilliseconds：方法执行超时时间，默认值是 1000，即 1秒，此值根据业务场景配置。
- execution.isolation.thread.interruptOnTimeout：是否在方法执行超时时中断方法。需要注意在 JVM 中我们无法强制中断一个线程，如果 Hystrix 方法里没有处理中断信号的逻辑，那么中断会被忽略。
- execution.isolation.thread.interruptOnCancel：是否在方法执行被取消时中断方法。
- execution.isolation.semaphore.maxConcurrentRequests：默认值是 10，此配置项要在 execution.isolation.strategy 配置为 semaphore 时才会生效，它指定了一个 Hystrix 方法使用信号量隔离时的最大并发数，超过此并发数的请求会被拒绝。信号量隔离的配置就这么一个，也是前文说信号量隔离配置不灵活的原因。

#### （2）熔断策略

- circuitBreaker.enabled：是否启用熔断器，默认为 true;
- circuitBreaker.forceOpen：是否强制启用熔断器，强制启用都想不到什么应用的场景，保持默认值，不配置即可。
- circuitBreaker.forceClosed：是否强制关闭熔断器，强制关闭都想不到什么应用的场景，保持默认值，不配置即可。
- circuitBreaker.requestVolumeThreshold：启用熔断器功能窗口时间内的最小请求数。试想如果没有这么一个限制，我们配置了 50% 的请求失败会打开熔断器，窗口时间内只有 3 条请求，恰巧两条都失败了，那么熔断器就被打开了，5s 内的请求都被快速失败。此配置项的值需要根据接口的 QPS 进行计算，值太小会有误打开熔断器的可能，值太大超出了时间窗口内的总请求数，则熔断永远也不会被触发。建议设置为 QPS * 窗口秒数 * 60%。
- circuitBreaker.errorThresholdPercentage：在通过滑动窗口获取到当前时间段内 Hystrix 方法执行的失败率后，就需要根据此配置来判断是否要将熔断器打开了。 此配置项默认值是 50，即窗口时间内超过 50% 的请求失败后会打开熔断器将后续请求快速失败。
- circuitBreaker.sleepWindowInMilliseconds：熔断器打开后，所有的请求都会快速失败，但何时服务恢复正常就是下一个要面对的问题。熔断器打开时，Hystrix 会在经过一段时间后就放行一条请求，如果这条请求执行成功了，说明此时服务很可能已经恢复了正常，那么会将熔断器关闭，如果此请求执行失败，则认为服务依然不可用，熔断器继续保持打开状态。此配置项指定了熔断器打开后经过多长时间允许一次请求尝试执行，默认值是 5000。

#### （3）回退策略

- fallback.enabled：是否启用方法回退，默认为 true 即可。
- fallback.isolation.semaphore.maxConcurrentRequests：回退方法执行时的最大并发数，默认是10，如果大量请求的回退方法被执行时，超出此并发数的请求会抛出 REJECTED_SEMAPHORE_FALLBACK 异常。

#### （4）

- requestLog.enabled：是否启用请求日志，默认为 true。

- requestCache.enabled：是否启用请求结果缓存。默认是 true，但它并不意味着我们的每个请求都会被缓存。缓存请求结果和从缓存中获取结果都需要我们配置 cacheKey，并且在方法上使用 @CacheResult 注解声明一个缓存上下文。

  

## 1.3 threadPoolProperties

### 配置方式

线程池的配置也是由 HystrixProperty 数组构成，配置方式与命令属性一致。

### 配置项

- coreSize：核心线程池的大小，默认值是 10，一般根据 QPS * 99% cost + redundancy count 计算得出。
- allowMaximumSizeToDivergeFromCoreSize：是否允许线程池扩展到最大线程池数量，默认为 false;
- maximumSize：线程池中线程的最大数量，默认值是 10，此配置项单独配置时并不会生效，需要启用 allowMaximumSizeToDivergeFromCoreSize 项。
- maxQueueSize：作业队列的最大值，默认值为 -1，设置为此值时，队列会使用 SynchronousQueue，此时其 size 为0，Hystrix 不会向队列内存放作业。如果此值设置为一个正的 int 型，队列会使用一个固定 size 的 LinkedBlockingQueue，此时在核心线程池内的线程都在忙碌时，会将作业暂时存放在此队列内，但超出此队列的请求依然会被拒绝。
- queueSizeRejectionThreshold：由于 maxQueueSize 值在线程池被创建后就固定了大小，如果需要动态修改队列长度的话可以设置此值，即使队列未满，队列内作业达到此值时同样会拒绝请求。此值默认是 5，所以有时候只设置了 maxQueueSize 也不会起作用。
- keepAliveTimeMinutes：由上面的 maximumSize，我们知道，线程池内核心线程数目都在忙碌，再有新的请求到达时，线程池容量可以被扩充为到最大数量，等到线程池空闲后，多于核心数量的线程还会被回收，此值指定了线程被回收前的存活时间，默认为 2，即两分钟。

### 工作方式

Hystrix 内线程池的使用是基于 Java 内置线程池的简单包装，通常有以下三种状态：

- 如果请求量少，达不到 coreSize，通常会使用核心线程来执行任务。
- 如果设置了 maxQueueSize，当请求数超过了 coreSize, 通常会把请求放到 queue 里，待核心线程有空闲时消费。
- 如果 queue 长度无法存储请求，则会创建新线程执行直到达到 maximumSize 最大线程数，多出核心线程数的线程会在空闲时回收。

























## 执行

1. **execution.isolation.strategy**

   设置HystrixCommand.run()的隔离策略，有两种选项

   > THREAD —— 在固定大小线程池中，以单独线程执行，并发请求数受限于线程池大小。
   >
   > SEMAPHORE —— 在调用线程中执行，通过信号量来限制并发量。

   默认值：THREAD（ExecutionIsolationStrategy.THREAD）

   可选值：THREAD，SEMAPHORE

   默认属性：hystrix.command.default.execution.isolation.strategy

   实例属性：hystrix.command.*HystrixCommandKey*.execution.isolation.strategy

2. **execution.timeout.enabled**

   设置 HystrixCommand.run() 的执行是否有超时限制。

   默认值：true

   默认属性：hystrix.command.default.execution.timeout.enabled

   实例属性：hystrix.command.*HystrixCommandKey*.execution.timeout.enabled

   实例默认的设置：

   ```java
   HystrixCommandProperties.Setter()
   .withExecutionTimeoutEnabled(boolean value)
   ```

   

3. **execution.isolation.thread.timeoutInMilliseconds**

   设置调用者等待命令执行的超时限制，超过此时间，HystrixCommand被标记为TIMEOUT，并执行回退逻辑。

   > 注意：超时会作用在***HystrixCommand.queue()\***，即使调用者没有调用***get()\***去获得Future对象。

   默认值：1000（毫秒）

   默认属性：hystrix.command.default.execution.isolation.thread.timeoutInMilliseconds

   实例属性：hystrix.command.*HystrixCommandKey*.execution.isolation.thread.timeoutInMilliseconds

   实例默认的设置：

   ```java
   HystrixCommandProperties.Setter()
   .withExecutionTimeoutInMilliseconds(int value)
   ```

   

4. **execution.isolation.thread.interruptOnTimeout**

   设置 HystrixCommand.run() 的执行是否在超时发生时被中断。

   默认值：true

   默认属性：hystrix.command.default.execution.isolation.thread.interruptOnTimeout

   实例属性：hystrix.command.*HystrixCommandKey*.execution.isolation.thread.interruptOnTimeout

   实例默认的设置：

   ```java
   HystrixCommandProperties.Setter()
   .withExecutionIsolationThreadInterruptOnTimeout(boolean value)
   ```

   

5. **execution.isolation.thread.interruptOnCancel**

   设置***HystrixCommand.run()***的执行但取消动作发生时候可以响应中断。

   默认值：false

   默认属性：hystrix.command.default.execution.isolation.thread.interruptOnCancel

   实例属性：hystrix.command.*HystrixCommandKey*.execution.isolation.thread.interruptOnCancel

   实例默认的设置：

   ```java
   HystrixCommandProperties.Setter()
   .withExecutionIsolationThreadInterruptOnCancel(boolean value)
   ```

   

6. **execution.isolation.semaphore.maxConcurrentRequests**

   设置当使用***ExecutionIsolationStrategy.SEMAPHORE***时，***HystrixCommand.run()***方法允许的最大请求数。如果达到最大并发数时，后续请求会被拒绝。

   信号量应该是容器（比如Tomcat）线程池一小部分，不能等于或者略小于容器线程池大小，否则起不到保护作用。

   默认值：10

   默认属性：hystrix.command.default.execution.isolation.semaphore.maxConcurrentRequests

   实例属性：hystrix.command.*HystrixCommandKey*.execution.isolation.semaphore.maxConcurrentRequests

   实例默认的设置：

   ```java
   HystrixCommandProperties.Setter()
   .withExecutionIsolationSemaphoreMaxConcurrentRequests(int value)
   ```

   

7. 234234

8. 2342

9. 234

10. 

    

## 回退

## 线程池

## 断路器

