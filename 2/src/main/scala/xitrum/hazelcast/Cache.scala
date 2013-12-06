package xitrum.hazelcast

import java.util.concurrent.TimeUnit
import com.hazelcast.core.{Hazelcast, IMap}
import xitrum.{Cache => XitrumCache}

class Cache extends XitrumCache {
  private[this] val cache = Hz.instance.getMap("xitrum/cache").asInstanceOf[IMap[Any, Any]]

  def start() {}

  def stop() {
    Hazelcast.shutdownAll()
  }

  def isDefinedAt(key: Any) = {
    cache.containsKey(key)
  }

  def get(key: Any) = Option(cache.get(key))

  def put(key: Any, value: Any) { cache.put(key, value) }

  def putIfAbsent(key: Any, value: Any) { cache.putIfAbsent(key, value) }

  def putSecond(key: Any, value: Any, seconds: Int) {
    cache.put(key.toString, value, seconds, TimeUnit.SECONDS)
  }

  def putSecondIfAbsent(key: Any, value: Any, seconds: Int) {
    cache.putIfAbsent(key.toString, value, seconds, TimeUnit.SECONDS)
  }

  def remove(key: Any) { cache.remove(key) }

  def clear() { cache.clear() }
}

