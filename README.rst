Optional module for `Xitrum <http://ngocdaothanh.github.io/xitrum/>`_
to provide cache and session store, based on Hazelcast.

Hazelcast 2.x is used at this moment, instead of latest 3.x, because 3.x is still
not as stable as 2.x.

Hazelcast 2.x supports 3 modes: cluster member, lite member, and native client.
But this module only supports cluster member or Java client mode, because
"lite member" mode is removed in Hazelcast 3.x anyway.

Usage
-----

Add hazelcast_cluster_member.xml (if you use cluster member mode) or
hazelcast_java_client.properties (if you use Java client mode) to the
config directory of your Xitrum project.

You can use both Hazelcast cache and session store, or only one.

Hazelcast cache
~~~~~~~~~~~~~~~

To use Hazelcast as the cache engine in your Xitrum project, edit xitrum.conf:

::

  xitrum {
    ...
    cache {
      "xitrum.hazelcast.Cache" {
        # clusterMember: hazelcast_cluster_member.xml is used
        # javaClient:    hazelcast_java_client.properties is used
        mode = clusterMember
      }
    }
    ...
  }

Hazelcast session store
~~~~~~~~~~~~~~~~~~~~~~~

To use Hazelcast as the session store in your Xitrum project, edit xitrum.conf:

::

  xitrum {
    ...
    session {
      store {
        "xitrum.hazelcast.Session" {
          # clusterMember: hazelcast_cluster_member.xml is used
          # javaClient:    hazelcast_java_client.properties is used
          mode = clusterMember
        }
      }
    }
    ...
  }
