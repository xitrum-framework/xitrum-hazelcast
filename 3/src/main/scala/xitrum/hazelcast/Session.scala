package xitrum.hazelcast

import com.hazelcast.core.{Hazelcast, IMap}
import xitrum.scope.session.ServerSessionStore

class Session extends ServerSessionStore {
  // lazy: see comment at Hz.instance
  private[this] lazy val store = Hz.instance.getMap("xitrum/session").asInstanceOf[IMap[String, Map[String, Any]]]

  def start() {}

  def stop() {
    Hazelcast.shutdownAll()
  }

  def get(sessionId: String): Option[Map[String, Any]] = Option(store.get(sessionId))

  def put(sessionId: String, immutableMap: Map[String, Any]) {
    // http://blog.hazelcast.com/2014/04/08/performance-top-5-1-map-put-vs-map-set/
    store.set(sessionId, immutableMap)
  }

  def remove(sessionId: String) {
    store.remove(sessionId)
  }
}
