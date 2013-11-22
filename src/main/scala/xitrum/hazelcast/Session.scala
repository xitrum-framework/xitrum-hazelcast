package xitrum.hazelcast

import com.hazelcast.core.{Hazelcast, IMap}

import xitrum.scope.session.ServerSessionStore

class HazelcastSessionStore extends ServerSessionStore {
  // We can use Cache, but we use a separate Hazelcast map to avoid the cost of
  // iterating through a big map as much as we can. Another reason is that the
  // application may need to config Hazelcast to persist sessions to a place
  // (disk, DB etc.) different to those for other things (cache, comet etc.).
  private[this] val store = Hz.instance.getMap("xitrum/session").asInstanceOf[IMap[String, Map[String, Any]]]

  def start() {}

  def stop() {
    Hazelcast.shutdownAll()
  }

  def get(sessionId: String): Option[Map[String, Any]] = Option(store.get(sessionId))

  def put(sessionId: String, immutableMap: Map[String, Any]) {
    store.put(sessionId, immutableMap)
  }

  def remove(sessionId: String) {
    store.remove(sessionId)
  }
}
