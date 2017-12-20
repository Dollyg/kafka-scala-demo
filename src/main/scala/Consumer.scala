import java.util

import org.apache.kafka.clients.consumer.KafkaConsumer
import java.util.Properties
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
