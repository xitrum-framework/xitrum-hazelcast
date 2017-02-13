organization := "tv.cntt"
name         := "xitrum-hazelcast2"
version      := "1.14.0-SNAPSHOT"

crossScalaVersions := Seq("2.12.1", "2.11.8")
scalaVersion       := "2.12.1"

scalacOptions ++= Seq("-deprecation", "-feature", "-unchecked")

// Xitrum requires Java 8
javacOptions ++= Seq("-source", "1.8", "-target", "1.8")

libraryDependencies += "tv.cntt"       %% "xitrum"           % "3.28.2" % "provided"
libraryDependencies += "com.hazelcast" %  "hazelcast"        % "2.6.10"  % "provided"
libraryDependencies += "com.hazelcast" %  "hazelcast-client" % "2.6.10"  % "provided"
