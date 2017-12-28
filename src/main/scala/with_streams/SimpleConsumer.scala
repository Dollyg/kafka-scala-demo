package with_streams

import akka.actor.ActorSystem
import akka.kafka.scaladsl.Consumer
import akka.kafka.{ConsumerSettings, Subscriptions}
import akka.stream.{ActorMaterializer, Materializer}
import com.typesafe.config.{Config, ConfigFactory}

object SimpleConsumer extends App{
  implicit val system: ActorSystem = ActorSystem("Basic")
  implicit val mat: Materializer = ActorMaterializer()

  private val config = ConfigFactory.load()
  private val consumerConf: Config = config.getConfig("akka.kafka.consumer")
  val consumerSettings: ConsumerSettings[Array[Byte], String] =
    ConsumerSettings(consumerConf, None, None)

  val subscription = Subscriptions.topics("new_topic")

  Consumer.plainSource(consumerSettings, subscription)
    .runForeach(println)

}

