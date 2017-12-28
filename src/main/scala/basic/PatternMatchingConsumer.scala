package basic

import java.util

import org.apache.kafka.clients.consumer.KafkaConsumer
import java.util.Properties
import java.util.regex.Pattern

import scala.collection.JavaConverters._

object PatternMatchingConsumer extends App {

  val props = new Properties()
  props.put("bootstrap.servers", "localhost:9092")
  props.put("key.deserializer", "org.apache.kafka.common.serialization.StringDeserializer")
  props.put("value.deserializer", "org.apache.kafka.common.serialization.StringDeserializer")
  props.put("group.id", "G_one")
  props.put("auto.offset.reset", "earliest")
  props.put("metadata.max.age.ms", "5")

  val TOPIC = "test"
  val consumer = new KafkaConsumer[String, String](props)  // gets all topics along with partitions + filter based on topics [Lazy evaluation, will be called on poll]
  val topicsToSubscribe = new util.ArrayList[String]
  topicsToSubscribe.add(TOPIC)
  consumer.subscribe(Pattern.compile("t.*"))

  while(true) {
    println("Before polling")
    val records = consumer.poll(1000)
    println("Polled")
    for (record <- records.asScala) {
      println("Consumed Record=", record)
    }
  }
  println("Halting..")
}
