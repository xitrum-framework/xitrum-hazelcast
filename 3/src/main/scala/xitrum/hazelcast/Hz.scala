package xitrum.hazelcast

import java.io.File

import com.hazelcast.client.HazelcastClient
import com.hazelcast.client.config.XmlClientConfigBuilder
import com.hazelcast.core.{Hazelcast, HazelcastInstance}

import xitrum.Config

object Hz {
  // http://hazelcast.org/docs/3.2/manual/html/logging.html
  System.setProperty("hazelcast.logging.type", "slf4j")

  // Use lazy to avoid starting Hazelcast if it is not used.
  // Starting Hazelcast takes several seconds, sometimes we want to work in
  // SBT console and don't touch Hazelcast, thus don't like this overhead.
  lazy val instance: HazelcastInstance = {
    val mode = Config.xitrum.config.getString("hazelcastMode")

    if (mode == "clusterMember") {
      val path = xitrum.root + File.separator + "config" + File.separator + "hazelcast_cluster_member.xml"
      System.setProperty("hazelcast.config", path)

      // null: load from "hazelcast.config" system property above
      // http://www.hazelcast.com/docs/3.0/manual/multi_html/ch12.html
      Hazelcast.newHazelcastInstance(null)
    } else {
      val clientConfig = new XmlClientConfigBuilder("hazelcast_java_client.xml").build()
      HazelcastClient.newHazelcastClient(clientConfig)
    }
  }
}

