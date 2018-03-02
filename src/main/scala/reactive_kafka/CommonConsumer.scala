package reactive_kafka

import akka.actor.ActorSystem
import akka.kafka.scaladsl.Consumer
import akka.kafka.{ConsumerSettings, KafkaConsumerActor, Subscriptions}
import akka.stream.{ActorMaterializer, Materializer}
import com.typesafe.config.{Config, ConfigFactory}
import org.apache.kafka.common.TopicPartition
import org.apache.kafka.common.serialization.{ByteArrayDeserializer, StringDeserializer}

object CommonConsumer extends App {
  implicit val system: ActorSystem = ActorSystem("Basic")
  implicit val mat: Materializer = ActorMaterializer()

  private val config = ConfigFactory.load()
  private val consumerConf: Config = config.getConfig("akka.kafka.consumer")
  val consumerSettings: ConsumerSettings[Array[Byte], String] =
    ConsumerSettings(system, new ByteArrayDeserializer, new StringDeserializer)

  val consumer = system.actorOf(KafkaConsumerActor.props(consumerSettings))
  val subscription = Subscriptions.assignment(Set(new TopicPartition("topic1", 0)))

  Consumer.plainExternalSource[Array[Byte], String](consumer, subscription)
    .runForeach(x => println("hello1"))

  Consumer.plainExternalSource[Array[Byte], String](consumer
    , subscription)
    .runForeach(x => println("hello2"))
}
