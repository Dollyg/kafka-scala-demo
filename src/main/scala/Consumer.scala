import java.util

import org.apache.kafka.clients.consumer.KafkaConsumer
import java.util.Properties

import org.apache.kafka.common.TopicPartition

import scala.collection.JavaConverters._

object Consumer extends App {

  val props = new Properties()
  props.put("bootstrap.servers", "localhost:9092")
  props.put("key.deserializer", "org.apache.kafka.common.serialization.StringDeserializer")
  props.put("value.deserializer", "org.apache.kafka.common.serialization.StringDeserializer")
  props.put("group.id", "G_one")
  props.put("auto.offset.reset", "earliest")

  val TOPIC = "test"
  val consumer = new KafkaConsumer[String, String](props)
  val topicsToSubscribe = new util.ArrayList[String]
  topicsToSubscribe.add(TOPIC)
  consumer.subscribe(topicsToSubscribe)
  consumer.poll(0) //polling as subscribe is lazy


  val topicPartitions = new util.ArrayList[TopicPartition]
  val topicPartition = new TopicPartition("test", 0)
  topicPartitions.add(topicPartition)

  val endoffsets = consumer.endOffsets(topicPartitions)
  println("End offsets=", endoffsets)
  val lastOffset = endoffsets.get(topicPartition)
  println("Last offset=", lastOffset)

  consumer.seek(topicPartition, lastOffset - 1) //get one message before latest

  while (true) {
    println("Before polling")
    val records = consumer.poll(1000)
    println("Polled")
    for (record <- records.asScala) {
      println("Consumed Record=", record)
    }
  }
  println("Halting..")
}
