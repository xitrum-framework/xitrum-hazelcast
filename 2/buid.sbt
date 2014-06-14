organization := "tv.cntt"

name := "xitrum-hazelcast2"

version := "1.8-SNAPSHOT"

scalaVersion := "2.11.1"
//scalaVersion := "2.10.4"

crossScalaVersions := Seq("2.11.1", "2.10.4")

scalacOptions ++= Seq("-deprecation", "-feature", "-unchecked")

// Xitrum requires Java 7
javacOptions ++= Seq("-source", "1.7", "-target", "1.7")

// Most Scala projects are published to Sonatype, but Sonatype is not default
// and it takes several hours to sync from Sonatype to Maven Central
resolvers += "SonatypeReleases" at "http://oss.sonatype.org/content/repositories/releases/"

libraryDependencies += "tv.cntt" %% "xitrum" % "3.14" % "provided"

libraryDependencies += "com.hazelcast" % "hazelcast" % "2.6.9"

libraryDependencies += "com.hazelcast" % "hazelcast-client" % "2.6.9"

//------------------------------------------------------------------------------

// Skip API doc generation to speedup "publish-local" while developing.
// Comment out this line when publishing to Sonatype.
publishArtifact in (Compile, packageDoc) := false
