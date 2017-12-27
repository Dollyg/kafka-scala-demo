package basic

import java.util.Properties

import org.apache.kafka.clients.producer.{KafkaProducer, ProducerRecord}

object Producer extends App {

  val props = new Properties()
  props.put("bootstrap.servers", "localhost:9092")
  props.put("key.serializer", "org.apache.kafka.common.serialization.StringSerializer")
  props.put("value.serializer", "org.apache.kafka.common.serialization.StringSerializer")

  val TOPIC = "test"
  val producer = new KafkaProducer[String, String](props)
  val record = new ProducerRecord(TOPIC, "key", "Hello world")
  producer.send(record)
  println("Produced record=", record)
  producer.close()
  println("connection closed.")
}