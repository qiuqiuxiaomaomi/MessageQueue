#Kafka
![](https://i.imgur.com/memfEGu.png)


#Rocketmq
![](https://i.imgur.com/jkQc4Sh.png)

<pre>
Kafka Producer配置解析

2017-08-07 10:44:14,660 [qtp22388680-31] [org.apache.kafka.clients.producer.ProducerConfig] [INFO] - ProducerConfig values: 
	acks = 1
	batch.size = 16384
	block.on.buffer.full = false
	bootstrap.servers = [127.0.0.1:9092, 127.0.0.2:9092, 127.0.0.3:9092]
	buffer.memory = 33554432
	client.id = 
	compression.type = none
	connections.max.idle.ms = 540000
	interceptor.classes = null
	key.serializer = class org.apache.kafka.common.serialization.StringSerializer
	linger.ms = 1
	max.block.ms = 60000
	max.in.flight.requests.per.connection = 5
	max.request.size = 1048576
	metadata.fetch.timeout.ms = 60000
	metadata.max.age.ms = 300000
	metric.reporters = []
	metrics.num.samples = 2
	metrics.sample.window.ms = 30000
	partitioner.class = class org.apache.kafka.clients.producer.internals.DefaultPartitioner
	receive.buffer.bytes = 32768
	reconnect.backoff.ms = 50
	request.timeout.ms = 30000
	retries = 10
	retry.backoff.ms = 100
	sasl.jaas.config = null
	sasl.kerberos.kinit.cmd = /usr/bin/kinit
	sasl.kerberos.min.time.before.relogin = 60000
	sasl.kerberos.service.name = null
	sasl.kerberos.ticket.renew.jitter = 0.05
	sasl.kerberos.ticket.renew.window.factor = 0.8
	sasl.mechanism = GSSAPI
	security.protocol = PLAINTEXT
	send.buffer.bytes = 131072
	ssl.cipher.suites = null
	ssl.enabled.protocols = [TLSv1.2, TLSv1.1, TLSv1]
	ssl.endpoint.identification.algorithm = null
	ssl.key.password = null
	ssl.keymanager.algorithm = SunX509
	ssl.keystore.location = null
	ssl.keystore.password = null
	ssl.keystore.type = JKS
	ssl.protocol = TLS
	ssl.provider = null
	ssl.secure.random.implementation = null
	ssl.trustmanager.algorithm = PKIX
	ssl.truststore.location = null
	ssl.truststore.password = null
	ssl.truststore.type = JKS
	timeout.ms = 30000
	value.serializer = class org.apache.kafka.common.serialization.StringSerializer
</pre>

<pre>
TimeoutException解析

  2017-07-20 17:10:41.044 [org.springframework.kafka.KafkaListenerEndpointContainer#0-0-L-1] ERROR o.s.k.s.LoggingProducerListener -Exception thrown when sending a message with key='null' and payload='{"topic":"media-asset-mzk","id":"ad674850-1bfd-01ba-f8ce-0d1ad56e0b55","body":{"application":"mzk-au...' to topic media-asset-mzk:
  org.apache.kafka.common.errors.TimeoutException: Failed to update metadata after 60000 ms.
</pre>

<pre>
以Kafka为中心的解决方案	
Kafka核心概念：
     Topic VS Log
	 Broker
	 副本
	 生产者
	 消费者

生产者 KafkaProducer
     KafkaProducer分析
     Sender分析	 
	 
消费者 
     KafkaConsumer

Kafka服务端
    1）网络层
    2）API层
    3）日志存储
    5）副本机制
    6）KafkaController
    7）GroupCoordinator
    8）身份认证与权限控制
    9）Kafka监控

KafkaTool
    kafka各种脚本	
</pre>

<pre>
Kafka为何这么快，深入分析

   1）写得快
      1：消息顺序写磁盘。
         新的消息只能追加到已有消息的末尾，并且已经生产的消息不支持随机删除以及随机访问。
      2：批量发送
         即使顺序写，过于频繁的大量小I/O操作一样会造成磁盘性能的瓶颈，所以Kafka在此处的
      处理是把这些消息集合在一起批量发送，这样减少磁盘I/O的过度读写，而不是一次发送单个消息。
      3：减少字节复制
         Kafka采用由Producer, Broker, Consumer共享的标准化二进制消息格式，这样数据块
      就可以在它们之间自由传输，无需转换，降低了字符复制的成本开销。
      5）MMAP 内存映射文件技术

   2）读得快
      1）零拷贝
         消息日志从页缓存传输到socket，通过sendfile系统调用来完成。
      2）批量压缩
         网络传输中，KAFKA使用批量压缩，将多个消息一起压缩。
         Kafka允许使用递归的消息集合，批量的消息可以通过压缩的形式传输并且在日志中也可以
      保持压缩格式，知道被消费者解压缩。
         Kafka支持Gzip和Snappy压缩协议。

</pre>

<pre>
Kafka集群恢复过程
</pre>

<pre>
Kafka性能调优

      1）网络和io操作线程配置优化

          # broker处理消息的最大线程数,默认为3，配置线程数为CPU核数 + 1 
          num.network.threads=xxx 

          # broker处理磁盘IO的线程数，配置需要大些，配置线程数为CPU核数2倍，最大不超过3倍。
          num.io.threads=xxx 

      2）log数据文件刷盘策略 

         # 每当producer写入10000条消息时，刷数据到磁盘 log.flush.interval.messages=10000

         # 每间隔1秒钟时间，刷数据到磁盘
         log.flush.interval.ms=1000

      3）日志保留策略配置

         当kafka server的被写入海量消息后，会生成很多数据文件，且占用大量磁盘空间，如果
      不及时清理，可能磁盘空间不够用，kafka默认是保留7天。

         # 保留三天，也可以更短 
         log.retention.hours=72

         # 段文件配置1GB，有利于快速回收磁盘空间，重启kafka加载也会加快(如果文件过小，则文件数量比较多，
         # kafka启动时是单线程扫描目录(log.dir)下所有数据文件)
         log.segment.bytes=1073741824

     Tips:
   
         Kafka官方并不建议通过Broker端的log.flush.interval.messages
     和log.flush.interval.ms来强制写盘，认为数据的可靠性应该通过Replica来保证，而强
     制Flush数据到磁盘会对整体性能产生影响。

     可以通过调整/proc/sys/vm/dirty_background_ratio和/proc/sys/vm/dirty_ratio来
     调优性能。

     脏页率超过第一个指标会启动pdflush开始Flush Dirty PageCache。
     脏页率超过第二个指标会阻塞所有的写操作来进行Flush。
     根据不同的业务需求可以适当的降低dirty_background_ratio和提高dirty_ratio。
     如果topic的数据量较小可以考虑减少log.flush.interval.ms和log.flush.interval.messages来强制刷写数据，减少可能由于缓存数据未写盘带来的不一
     致

     5) Replica

        replica.lag.time.max.ms:10000

        replica.lag.max.messages:4000

        num.replica.fetchers:1
 
        #在Replica上会启动若干Fetch线程把对应的数据同步到本地，而
             num.replica.fetchers这个参数是用来控制Fetch线程的数量。
        #每个Partition启动的多个Fetcher，通过共享offset既保证了同一时间内Consumer
        和Partition之间的一对一关系，又允许我们通过增多Fetch线程来提高效率。

        default.replication.factor:1
        #这个参数指新创建一个topic时，默认的Replica数量
        #Replica过少会影响数据的可用性，太多则会白白浪费存储资源，一般建议在2~3为宜

    6) Broker配置

        num.partitions:1
        #分区数量

		queued.max.requests:500
		#这个参数是指定用于缓存网络请求的队列的最大容量，这个队列达到上限之后将不再接收新请
	    求。一般不会成为瓶颈点，除非I/O性能太差，这时需要配合num.io.threads等配置一同进行调整。
		
		compression.codec:none
		#Message落地时是否采用以及采用何种压缩算法。一般都是把Producer发过来Message直接保
	    存，不再改变压缩方式。
		
		in.insync.replicas:1
		#这个参数只能在topic层级配置，指定每次Producer写操作至少要保证有多少个在ISR的
	    Replica确认，一般配合request.required.acks使用。要注意，这个参数如果设置的过高
        可能会大幅降低吞吐量。

     7）Producer端
       
        buffer.memory:33554432 (32m)
        #在Producer端用来存放尚未发送出去的Message的缓冲区大小。缓冲区满了之后可以选择
        阻塞发送或抛出异常，由block.on.buffer.full的配置来决定。

		compression.type:none
		#默认发送不进行压缩，推荐配置一种适合的压缩算法，可以大幅度的减缓网络压力和
         Broker的存储压力。
		
		
		linger.ms:0
		#Producer默认会把两次发送时间间隔内收集到的所有Requests进行一次聚合然后再发送，
        以此提高吞吐量，而linger.ms则更进一步，这个参数为每次发送增加一些delay，以此来
        聚合更多的Message。
		
		batch.size:16384
		#Producer会尝试去把发往同一个Partition的多个Requests进行合并，batch.size指
         明了一次Batch合并后Requests总大小的上限。如果这个值设置的太小，可能会导致所有
         的Request都不进行Batch。
		
		acks:1
		#这个配置可以设定发送消息后是否需要Broker端返回确认。
		
		0: 不需要进行确认，速度最快。存在丢失数据的风险。
		1: 仅需要Leader进行确认，不需要ISR进行确认。是一种效率和安全折中的方式。
		all: 需要ISR中所有的Replica给予接收确认，速度最慢，安全性最高，但是由于ISR可能会缩小到仅包含一个Replica，所以设置参数为all并不能一定避免数据丢失。

    8) Consumer配置

       num.consumer.fetchers:1
		#启动Consumer的个数，适当增加可以提高并发度。
		
		
		fetch.min.bytes:1
		#每次Fetch Request至少要拿到多少字节的数据才可以返回。
		
		#在Fetch Request获取的数据至少达到fetch.min.bytes之前，允许等待的最大时长。对
         应上面说到的Purgatory中请求的超时时间。
		fetch.wait.max.ms:100
</pre>

<pre>
高性能服务器模型:
      Kafka并没有采用netty或mina等第三方网络应用框架，而是直接了当的使用了NIO来实现服
      务器，并在使用了IO多路复用以及多线程Reactor模式，这种设计的优势是很容易实现，同时也很快

      1) Acceptor线程：主要负责监听并接受客户端（包括但不限于Producer，
         Consumer，Broker，Controller，AdminTool）的连接请求，新连接建立以后指定
         某个Processor去处理。

      2) Processor线程：负责数据读写，连接关闭的处理线程，其数目由配
         置num.network.threads决定，默认是3个。每个Processor内部都有自己
         的newConnections队列和selector。

             1) newConnections：一个无界的SocketChannel队列，存放新建立的连接，
             将Acceptor与Processor的功能解藕。
             2) selector：只使用一个selector来支撑大量连接的事件管理很容易遇到瓶颈，而多个selector并存的结构可以均衡的管理大量连接。

      3) RequestChannel：包含一个RequestQueue和多个ResponseQueue。它是网络层与API层
         交换数据的地方，同时也使得两者逻辑解藕和异步化。

         RequestQueue：所有的请求都会被封装成Request并放入RequestQueue中，队列大小默
                       认500。
         ResponseQueue：每个Processor都会有对应ResponseQueue，KafkaApis业务逻辑处理
                       完成后，会将返回结果封装成Response，接着由相应的Processor来处
                       理该response。而且设计上必须保证一对Request和Response都要由
                       同一个Processor来处理，因为只有这个Processor拥有该通信连接。


       5) KafkaRequestHandler线程：这是真正的业务逻辑处理线程，其数目由配
                                  置num.io.threads决定，默认是8个。每个Handler线程
                                  都在不断的从RequestChannel.RequestQueue中获取
                                  新的请求，那些负载轻的线程才有可能抢到新的请求，因
                                  为负载重的线程（也许正在进行IO）还没有空闲来接受下
                                  一个新的请求，所以这也算一个潜在的负载均衡策略吧。
       6) KafkaApis：Broker的所有业务逻辑都定义在这里，其handle方法会根据Request对象
                    的requestId（对应各种业务逻辑，其定义可以在类RequestKeys中看
                    到），将请求分发给对应的业务逻辑处理方法。当处理完成以后，可能会将处
                    理结果封装成Response返回给对应的RequestChannel.ResponseQueue。
</pre>

<pre>
Kafka Purgatory 炼狱
</pre>