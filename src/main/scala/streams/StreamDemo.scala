package streams

import akka.actor.ActorSystem
import akka.stream.ActorMaterializer
import akka.stream.scaladsl.Source
import akka.{Done, NotUsed}

import scala.concurrent.{ExecutionContextExecutor, Future}


object StreamDemo extends App {
  private val source: Source[Int, NotUsed] = Source(1 to 100)
  println(source)
  implicit private val system: ActorSystem = ActorSystem()
  implicit private val materializer: ActorMaterializer = ActorMaterializer()
  private val done: Future[Done] = source.runForeach(x => println("##", x))

  implicit val ec: ExecutionContextExecutor = system.dispatcher
  done.onComplete(_ => system.terminate())


}

