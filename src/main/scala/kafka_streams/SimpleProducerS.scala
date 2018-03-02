package kafka_streams

import java.util.Properties
import java.util.regex.Pattern

import com.lightbend.kafka.scala.streams.{KTableS, StreamsBuilderS}
import org.apache.kafka.common.serialization.{Serde, Serdes}
import org.apache.kafka.streams.kstream.Produced
import org.apache.kafka.streams.{KafkaStreams, StreamsConfig}


object SimpleProducerS extends App {
  val stringSerde = Serdes.String()
  val longSerde: Serde[Long] = Serdes.Long().asInstanceOf[Serde[Long]]
  val brokers = "localhost:9092"

  val streamsConfiguration = new Properties()
  streamsConfiguration.put(StreamsConfig.APPLICATION_ID_CONFIG, s"wordcount")

  streamsConfiguration.put(StreamsConfig.BOOTSTRAP_SERVERS_CONFIG, brokers)
  streamsConfiguration.put(StreamsConfig.DEFAULT_KEY_SERDE_CLASS_CONFIG, Serdes.String().getClass().getName())
  streamsConfiguration.put(StreamsConfig.DEFAULT_VALUE_SERDE_CLASS_CONFIG, Serdes.String().getClass().getName())

  val builder = new StreamsBuilderS()
  val textLines = builder.stream[String, String]("topic1")
  val pattern = Pattern.compile("\\W+", Pattern.UNICODE_CHARACTER_CLASS)

  val wordCounts: KTableS[String, Long] =
    textLines.flatMapValues(v => pattern.split(v.toLowerCase))
      .groupBy{(k, v) => println(k,v);v}
      .count()

  wordCounts.toStream.to("topic2", Produced.`with`(stringSerde, longSerde))

  val streams = new KafkaStreams(builder.build, streamsConfiguration)
  streams.start()

}
