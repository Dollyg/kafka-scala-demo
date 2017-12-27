package with_streams

import akka.Done
import akka.actor.ActorSystem
import akka.kafka._
import akka.kafka.scaladsl.{Consumer, Producer}
import akka.stream.{ActorMaterializer, Materializer}
import akka.stream.scaladsl.Source
import com.typesafe.config.{Config, ConfigFactory}
import org.apache.kafka.clients.producer.ProducerRecord

import scala.concurrent.Future

object BasicProducer extends App {
  implicit val system: ActorSystem = ActorSystem("Basic")
  implicit val mat: Materializer = ActorMaterializer()

  private val config = ConfigFactory.load()
  val producerConf: Config = config.getConfig("akka.kafka.producer")
  val producerSettings: ProducerSettings[Array[Byte], String] = ProducerSettings(producerConf, None, None)

  val done: Future[Done] = Source(200 to 300)
    .map(_.toString)
    .map { elem =>
      new ProducerRecord[Array[Byte], String]("topic1", elem)
    }.runWith(Producer.plainSink(producerSettings))

}
