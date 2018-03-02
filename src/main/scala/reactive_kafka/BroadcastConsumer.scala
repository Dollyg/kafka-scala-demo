package reactive_kafka

import akka.actor.ActorSystem
import akka.kafka.scaladsl.Consumer
import akka.kafka.{ConsumerSettings, Subscriptions}
import akka.stream.scaladsl.{BroadcastHub, Keep}
import akka.stream.{ActorMaterializer, Materializer}
import com.typesafe.config.{Config, ConfigFactory}
import org.apache.kafka.common.serialization.{ByteArrayDeserializer, StringDeserializer}

object BroadcastConsumer extends App {
  implicit val system: ActorSystem = ActorSystem("Basic")
  implicit val mat: Materializer = ActorMaterializer()

  private val config = ConfigFactory.load()
  private val consumerConf: Config = config.getConfig("akka.kafka.consumer")

  val consumerSettings: ConsumerSettings[Array[Byte], String] =
    ConsumerSettings(system, new ByteArrayDeserializer, new StringDeserializer)

  val subscription = Subscriptions.topics("topic1")

  private val source = Consumer.plainSource(consumerSettings.withGroupId("a"), subscription)

  private val producer = source.toMat(BroadcastHub.sink(256))(Keep.right).run()

  producer.runForeach(x => println("hello1"))
  producer.runForeach(x => println("hello2"))
}
