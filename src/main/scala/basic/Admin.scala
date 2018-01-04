package basic

import java.util.Properties


import kafka.admin.AdminUtils
import kafka.utils.{ZkUtils, ZooKeeperClientWrapper}
import org.I0Itec.zkclient.{ZkClient, ZkConnection}
import org.apache.kafka.clients.admin.NewTopic

object Admin extends App{

  private val helloWorldTopic = new NewTopic("helloWorld", 1, 1)
  val ZOOKEEPER_CONNECT = "localhost:2181"
  private val (zkclient, connection): (ZkClient, ZkConnection) = ZkUtils.createZkClientAndConnection(ZOOKEEPER_CONNECT, 1000, 1000)

  private val zkUtils = ZkUtils(zkclient, false)

  AdminUtils.createTopic(zkUtils, "new10101", 1, 1, new Properties())


}
