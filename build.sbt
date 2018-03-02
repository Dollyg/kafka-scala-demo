name := "kafka-scala-demo"
version := "0.1"
scalaVersion := "2.12.4"
lazy val akkaVersion = "2.5.6"

resolvers += Resolver.bintrayRepo("cakesolutions", "maven")

libraryDependencies ++= Seq(
  "com.typesafe.akka" %% "akka-actor" % akkaVersion,
  "org.apache.kafka" %% "kafka" % "1.0.0",
  "org.apache.kafka" % "kafka-clients" % "1.0.0",
  "net.cakesolutions" %% "scala-kafka-client-akka" % "1.0.0",
  "com.typesafe.akka" %% "akka-stream-kafka" % "0.18",
  "com.lightbend" %% "kafka-streams-scala" % "0.1.0",
  "com.lightbend" %% "kafka-streams-query" % "0.1.0"

)
