package with_akka

import akka.actor.{Actor, ActorRef, ActorSystem, Props}
import cakesolutions.kafka.akka.KafkaConsumerActor.{Confirm, Subscribe}
import cakesolutions.kafka.akka.{ConsumerRecords, KafkaConsumerActor, KafkaProducerActor, ProducerRecords}
import cakesolutions.kafka.{KafkaConsumer, KafkaProducer}
import com.typesafe.config.ConfigFactory
import org.apache.kafka.clients.consumer.OffsetResetStrategy
import org.apache.kafka.common.serialization.{StringDeserializer, StringSerializer}

object Demo extends App {
  val cfg = ConfigFactory.load()
  val system = ActorSystem("Demo")

  private val TOPIC = "topic1"
  val producerConfig = cfg.getConfig("producer")
  val producerConf = KafkaProducer.Conf(new StringSerializer, new StringSerializer).withConf(producerConfig)
  val producer = system.actorOf(KafkaProducerActor.props(producerConf))
  val producerRecords = ProducerRecords.fromKeyValues[String, String](TOPIC, Seq((Some("key"), "helloooo")), None, None)

  val downstreamActor = system.actorOf(Props(classOf[DownstreamActor], producer, producerRecords))
  producer ! producerRecords
  println("Produced records...")

}

class DownstreamActor(p: ActorRef, r: ProducerRecords[String, String]) extends Actor {
  private val cfg = ConfigFactory.load()

  private val consumerConfig = cfg.getConfig("consumer")
  val actorConfig = KafkaConsumerActor.Conf()

  private val kafkaConfig = KafkaConsumer.Conf(
    new StringDeserializer,
    new StringDeserializer,
    groupId = "test_group",
    enableAutoCommit = false,
    autoOffsetReset = OffsetResetStrategy.LATEST)
    .withConf(consumerConfig)


  private val consumer = context.actorOf(KafkaConsumerActor.props(kafkaConfig, actorConfig, self))
  consumer ! Subscribe.AutoPartition(List("topic1"))
  println("Subscribed...")

  override def receive: Receive = {
    case t: ConsumerRecords[_, _] =>
      println(s"Message=${t.pairs}")
      sender() ! Confirm(t.offsets)
  }
}
