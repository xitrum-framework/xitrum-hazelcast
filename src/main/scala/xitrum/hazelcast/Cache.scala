package xitrum.hazelcast

import com.hazelcast.client.{ClientConfig, ClientConfigBuilder, HazelcastClient}
import com.hazelcast.core.{Hazelcast, HazelcastInstance}

/**
 * Use lazy to avoid starting Hazelcast if it is not used.
 * Starting Hazelcast takes several seconds, sometimes we want to work in
 * sbt console mode and don't like this overhead.
 */
lazy val hazelcastInstance: HazelcastInstance = {
  // http://www.hazelcast.com/docs/2.6/manual/multi_html/ch12s07.html
  System.setProperty("hazelcast.logging.type", "slf4j")

  if (xitrum.hazelcastMode == HAZELCAST_MODE_CLUSTER_MEMBER) {
    val path = Config.root + File.separator + "config" + File.separator + "hazelcast_cluster_member.xml"
    System.setProperty("hazelcast.config", path)

    // null: load from "hazelcast.config" system property above
    // http://www.hazelcast.com/docs/2.6/manual/multi_html/ch12.html
    Hazelcast.newHazelcastInstance(null)
  } else {
    // https://github.com/hazelcast/hazelcast/issues/93
    val clientConfig = new ClientConfigBuilder("hazelcast_java_client.properties").build()
    HazelcastClient.newHazelcastClient(clientConfig)
  }
}



Hazelcast.shutdownAll()


-------------

package xitrum

import java.util.concurrent.TimeUnit
import java.util.Map.Entry
import scala.util.control.NonFatal

import com.hazelcast.core.{IMap, MapEntry}
import com.hazelcast.query.Predicate

object Cache extends Logger {
  val cache = Config.hazelcastInstance.getMap("xitrum/cache").asInstanceOf[IMap[String, Any]]

  def remove(key: Any) {
    cache.removeAsync(key.toString)
  }

  def removeAction(actionClass: Class[_ <: Action]) {
    val keyPrefix = pageActionPrefix(actionClass)
    removePrefix(keyPrefix)
  }

  def pageActionPrefix(actionClass: Class[_ <: Action]): String =
    "xitrum/page-action/" + actionClass.getName

  private def removePrefix(keyPrefix: Any) {
    val keyPrefixS = keyPrefix.toString
    val prefixPredicate = new Predicate[String, Any] {
      def apply(mapEntry: MapEntry[String, Any]) = mapEntry.getKey.startsWith(keyPrefixS)
    }

    val keys = cache.keySet(prefixPredicate)
    val it = keys.iterator
    while (it.hasNext()) {
      val key = it.next()
      cache.removeAsync(key)
    }
  }

  //---------------------------------------------------------------------------

  def put(key: Any, value: Any) {
    if (logger.isDebugEnabled) logger.debug("Cache put: " + key)
    cache.putAsync(key.toString, value)
  }

  def putSecond(key: Any, value: Any, seconds: Int) {
    if (logger.isDebugEnabled) logger.debug("Cache put (" + seconds + "s): " + key)
    cache.put(key.toString, value, seconds, TimeUnit.SECONDS)
  }
  def putMinute(key: Any, value: Any, minutes: Int) { putSecond(key, value, minutes * 60) }
  def putHour  (key: Any, value: Any, hours:   Int) { putMinute(key, value, hours   * 60) }
  def putDay   (key: Any, value: Any, days:    Int) { putHour  (key, value, days    * 24) }

  def putIfAbsent(key: Any, value: Any) {
    if (logger.isDebugEnabled) logger.debug("Cache putIfAbsent: " + key)
    cache.putIfAbsent(key.toString, value)
  }

  def putIfAbsentSecond(key: Any, value: Any, seconds: Int) {
    if (logger.isDebugEnabled) logger.debug("Cache putIfAbsent (" + seconds + "s): " + key)
    cache.putIfAbsent(key.toString, value, seconds, TimeUnit.SECONDS)
  }
  def putIfAbsentMinute(key: Any, value: Any, minutes: Int) { putIfAbsentSecond(key, value, minutes * 60) }
  def putIfAbsentHour  (key: Any, value: Any, hours:   Int) { putIfAbsentMinute(key, value, hours   * 60) }
  def putIfAbsentDay   (key: Any, value: Any, days:    Int) { putIfAbsentHour  (key, value, days    * 24) }

  //---------------------------------------------------------------------------

  /**
   * Gets data from cache with type cast.
   * Application version up etc. may cause cache restoring to be failed.
   * In this case, we remove the cache.
   */
  def getAs[T](key: Any): Option[T] = {
    if (!Config.productionMode) return None

    try {
      Option(cache.get(key)).map(_.asInstanceOf[T])
    } catch {
      case NonFatal(e) =>
        logger.warn("Cache data restoring failed, will now remove it, key: {}", key)
        cache.remove(key)
        None
    }
  }

  def tryCacheSecond[T](key: Any, secs: Int)(f: => T): T = {
    getAs[T](key).getOrElse {
      val value = f
      putIfAbsentSecond(key, value, secs)
      value
    }
  }

  def tryCacheMinute[T](key: String, minutes: Int)(f: => T): T = tryCacheSecond(key, minutes * 60)(f)
  def tryCacheHour[T]  (key: String, hours:   Int)(f: => T): T = tryCacheMinute(key, hours   * 60)(f)
  def tryCacheDay[T]   (key: String, days:    Int)(f: => T): T = tryCacheHour  (key, days    * 24)(f)
}
