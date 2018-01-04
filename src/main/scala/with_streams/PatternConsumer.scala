package with_streams

import akka.actor.ActorSystem
import akka.kafka.scaladsl.Consumer
import akka.kafka.{AutoSubscription, ConsumerSettings, Subscription, Subscriptions}
import akka.stream.{ActorMaterializer, Materializer}
import com.typesafe.config.{Config, ConfigFactory}

object PatternConsumer extends App{
  implicit val system: ActorSystem = ActorSystem("Basic")
  implicit val mat: Materializer = ActorMaterializer()

  private val config = ConfigFactory.load()
  private val consumerConf: Config = config.getConfig("akka.kafka.consumer")
  val consumerSettings: ConsumerSettings[Array[Byte], String] =
    ConsumerSettings(consumerConf, None, None)

  //WIP
  private val subscription: AutoSubscription = Subscriptions.topicPattern("H.*")
  Consumer.plainSource(consumerSettings, subscription)
    .runForeach(println)

}

