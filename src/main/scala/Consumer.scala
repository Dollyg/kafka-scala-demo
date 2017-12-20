import java.util

import org.apache.kafka.clients.consumer.KafkaConsumer
import java.util.Properties

import org.apache.kafka.common.TopicPartition

import scala.collection.JavaConverters._

object Consumer extends App {

  val props = new Properties()
  props.put("bootstrap.servers", "localhost:9092")
  props.put("key.deserializer",
    "org.apache.kafka.common.serialization.StringDeserializer")
  props.put("value.deserializer",
    "org.apache.kafka.common.serialization.StringDeserializer")
  props.put("group.id", "G_one")
  props.put("auto.offset.reset", "earliest")

  val TOPIC1 = "test1"
  val TOPIC2 = "test2"
  val consumer = new KafkaConsumer[String, String](props)

  val topicsToSubscribe = List(TOPIC1, TOPIC2)
  consumer.subscribe(topicsToSubscribe.asJava)
  consumer.poll(0) //polling as subscribe is lazy

  val topicPartition1 = new TopicPartition(TOPIC1, 0)
  val topicPartitions = List(topicPartition1).asJava

  val endoffsets = consumer.endOffsets(topicPartitions)
  println("End offsets=", endoffsets)
  val lastOffset = endoffsets.get(topicPartition1)
  println("Last offset=", lastOffset)

  consumer.seek(topicPartition1, lastOffset - 1) //get one message before latest

  for (i <- 1 to 50) {
    println("Before polling")
    val records = consumer.poll(1000)
    println("Polled")
    for (record <- records.asScala) {
      println("Consumed Record=", record)
    }

    if (i == 25) {
      println("Topics before=",consumer.subscription())
      val topicsToUnsubscribe = List(TOPIC2)
      val newTopicsToSubscribe = topicsToSubscribe.diff(topicsToUnsubscribe)
      consumer.subscribe(newTopicsToSubscribe.asJava)
      consumer.poll(0) //polling as subscribe is lazy
      println("Topics after=",consumer.subscription())
    }
  }
  println("Halting..")
}
