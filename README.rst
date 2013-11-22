Optional module for `Xitrum <http://ngocdaothanh.github.io/xitrum/>`_
to provide cache and session store, based on Hazelcast.

Usage
-----

Add dependency to your project:

::

  libraryDependencies += "tv.cntt" %% "xitrum-hazelcast" % "1.0"

You can use Hazelcast for Xitrum cache or Xitrum session store.

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

Config Hazelcast
----------------

Above is the config for Xitrum. This section discusses about configuring Hazelcast itself.

In the config directory, there are 2 sample config files:

* hazelcast_cluster_member.xml: Use if you use Hazelcast cluster member mode
* hazelcast_java_client.properties: Use if you use Hazelcast Java client mode

Copy the file that you need to the config directory of your Xitrum project.

Note that Xitrum instances in the same group (cluster) should have the same
``<group>/<name>``. Hazelcast provides a monitor tool, ``<group>/<password>``
is the password for the tool to connect to the group.

Read `Hazelcast doc <http://hazelcast.com/docs.jsp>`_ to know more about the modes above and
how to config them.

Version
-------

Hazelcast 2.x is used at this moment, instead of latest 3.x, because 3.x is still
not as stable as 2.x.

Hazelcast 2.x supports 3 modes: cluster member, lite member, and native client.
But this module only supports cluster member or Java client mode, because
"lite member" mode is removed in Hazelcast 3.x anyway.

xitrum-hazelcast 1.0:

* Xitrum 1.12
* Hazelcast 2.6.5
