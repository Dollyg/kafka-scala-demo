package with_streams

import akka.actor.ActorSystem
import akka.kafka.scaladsl.Consumer
import akka.kafka.{ConsumerSettings, Subscriptions}
import akka.stream.{ActorMaterializer, Materializer}
import com.typesafe.config.{Config, ConfigFactory}
import org.apache.kafka.clients.consumer.KafkaConsumer
import org.apache.kafka.common.TopicPartition

object OffsetConsumer extends App{
  implicit val system: ActorSystem = ActorSystem("Basic")
  implicit val mat: Materializer = ActorMaterializer()

  private val config = ConfigFactory.load()
  private val consumerConf: Config = config.getConfig("akka.kafka.consumer")
  val consumerSettings: ConsumerSettings[Array[Byte], String] =
    ConsumerSettings(consumerConf, None, None)

  private val topicPartition = new TopicPartition("new_topic", 0)
  private val kafkaConsumer: KafkaConsumer[Array[Byte], String] = consumerSettings.createKafkaConsumer()
  private val endOffsets = kafkaConsumer.endOffsets(java.util.Collections.singleton(topicPartition))
  val lastOffset = endOffsets.get(topicPartition) - 1

  val subscription = Subscriptions.assignmentWithOffset(topicPartition,lastOffset)

  Consumer.plainSource(consumerSettings, subscription)
    .runForeach(println)

}

