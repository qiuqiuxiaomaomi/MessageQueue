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