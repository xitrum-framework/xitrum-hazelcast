Optional module for `Xitrum <http://xitrum-framework.github.io/>`_
to provide cache and session store, based on `Hazelcast <http://www.hazelcast.com/>`_.

Both Hazelcast 2.x and 3.x are supported.

See CHANGELOG for info about specific Xitrum and Hazelcast versions.

Configure SBT
-------------

Add dependency to your SBT project:

Hazelcast 2:

::

  libraryDependencies += "tv.cntt"       %% "xitrum-hazelcast2" % "1.14.0"
  libraryDependencies += "com.hazelcast" %  "hazelcast"         % "2.6.9"
  libraryDependencies += "com.hazelcast" %  "hazelcast-client"  % "2.6.9"

Hazelcast 3:

::

  libraryDependencies += "tv.cntt"       %% "xitrum-hazelcast3" % "1.14.0"
  libraryDependencies += "com.hazelcast" %  "hazelcast"         % "3.7.5"
  libraryDependencies += "com.hazelcast" %  "hazelcast-client"  % "3.7.5"


You can use Hazelcast for Xitrum cache or Xitrum session store.

Hazelcast cache
~~~~~~~~~~~~~~~

To use Hazelcast as the cache engine in your Xitrum project, edit xitrum.conf:

::

  xitrum {
    ...
    # clusterMember: hazelcast_cluster_member.xml is used
    # javaClient:    hazelcast_java_client.properties is used
    hazelcastMode = clusterMember

    cache = xitrum.hazelcast.Cache
    ...
  }

Hazelcast session store
~~~~~~~~~~~~~~~~~~~~~~~

To use Hazelcast as the session store in your Xitrum project, edit xitrum.conf:

::

  xitrum {
    ...
    # clusterMember: hazelcast_cluster_member.xml is used
    # javaClient:    hazelcast_java_client.properties is used
    hazelcastMode = clusterMember

    session {
      store = xitrum.hazelcast.Session
      ...
    }
    ...
  }

Configure Hazelcast
-------------------

Above is the config for Xitrum. This section instructs about configuring
Hazelcast itself.

In the config directory, there are 2 sample config files:

* hazelcast_cluster_member.xml: Use if you use Hazelcast cluster member mode
* hazelcast_java_client.properties (Hazelcast 2) or hazelcast_java_client (Hazelcast 3):
  Use if you use Hazelcast Java client mode

Copy the file that you need to the config directory of your Xitrum project.

Xitrum instances in the same cluster should have the same ``<group>/<name>``.
Hazelcast provides `a tool <http://www.hazelcast.com/mancenter.jsp>`_ to easily
monitor your cluster.

Hazelcast 2.x supports 3 modes: cluster member, lite member, and native client.
But because "lite member" mode is removed in Hazelcast 3.x, xitrum-hazelcast
only supports cluster member and Java client modes. Read `Hazelcast doc <http://www.hazelcast.com/docs.jsp>`_
to know more about the modes above and how to configure them.

Configure log
-------------

You may want to add this line to file config/logback.xml in your Xitrum project,
to avoid noisy Hazelcast log:

::

  <logger name="com.hazelcast" level="INFO"/>
