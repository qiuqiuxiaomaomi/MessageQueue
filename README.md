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