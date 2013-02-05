# hiroko

<img src="http://i6.photobucket.com/albums/y207/elicecafe/202.jpg" align="center"/>

A scala interface for provisioning [heroku.com][heroku] hosted applications.

## install

Via sbt

Append the following to your build definition

    libraryDependencies += "me.lessis" %% "hiroko" % "0.1.0"

Via [ls-sbt](http://ls.implicit.ly/#installing)

In your sbt terminal type

    > ls-install hiroko

## usage

Grab your [api key](https://dashboard.heroku.com/account).

Hiroko provides a request builder interface that wraps [dispatch][dispatch] requests so everything in your dispatch toolbox
should "just work" with Hiroko.

The dispatch [Json4s][json4s] interface will already be on your classpath but you are
free to use any dispatch handler to handle api responses.

All you need to get started is to import the hiroko package members and create a client with your api key.

```scala
import hiroko._
val cli = Client(apiKey)
```

```scala
import dispatch._
import org.json4s._

val apps = for {
  JArray(ary)             <- cli.apps(as.json4s.Json)()
  JObject(fs)             <- ary
  ("name", JString(name)) <- fs
} yield name

apps.foreach(println)

val created = for {
  JObject(fs)             <- cli.apps.create(as.json4s.Json)()
  ("name", JString(name)) <- fs
} yield name

created.headOption { name =>
  println("created app %s" format name)
  cli.app(name).destroy(as.json4s.Json)()
}
```


Doug Tangren (softprops) 2013

[herokuapi]: https://api-docs.heroku.com/
[heroku]: http://heroku.com
[dispatch]: http://dispatch.databinder.net/Dispatch.html
[json4s]: http://json4s.org/
