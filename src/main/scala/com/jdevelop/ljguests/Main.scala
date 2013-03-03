package com.jdevelop.ljguests

import java.util.{TimeZone, Date}
import java.io.{Writer, FileWriter, File}
import util.parsing.json.{JSONObject, JSON}
import java.text.SimpleDateFormat

/**
 * User: Eugene Dzhurinsky
 * Date: 3/2/13
 */
object Main {

  private case class Config(username: String,
                            password: String,
                            lastDate: Option[Date],
                            tz: String)

  private class ClassCaster[T] {
    def unapply(a: Any): Option[T] = Some(a.asInstanceOf[T])
  }

  private object AsString extends ClassCaster[String]

  private object ConfigStorage {

    def sdf(tz: String) = {
      val timezone = new SimpleDateFormat("dd-MM-yyyy")
      timezone.setTimeZone(TimeZone.getTimeZone(tz))
      timezone
    }

    val home = new File(sys.env("HOME"))

    val confFile = new File(home, ".ljguests")

    def read(): Option[Config] = {
      require(confFile.exists(), "Configuration file '" + confFile + "' does not exist")
      JSON.parseFull(io.Source.fromFile(confFile).mkString) flatMap {
        case objMap: Map[String, Any] =>
          for (
            AsString(uname) <- objMap.get("username");
            AsString(passwd) <- objMap.get("password");
            tmz = objMap.getOrElse("timeZone", "EST").asInstanceOf[String]
          ) yield Config(uname,
            passwd,
            objMap.get("lastDate").map((x: Any) => sdf(tmz).parse(x.toString)),
            tmz
          )
        case _ => None
      }
    }

    def write(cfg: Config) {
      val mainData: Map[String, Any] = Map(
        "username" -> cfg.username,
        "password" -> cfg.password,
        "timeZone" -> cfg.tz
      )
      val content = new JSONObject(
        cfg.lastDate.map((z: Date) => mainData + ("lastDate" -> sdf(cfg.tz).format(z))).getOrElse(mainData)
      ).toString()
      var writer: Writer = null
      try {
        writer = new FileWriter(confFile)
        writer.write(content)
        writer.flush()
      } finally {
        writer.close()
      }
    }

  }

  def main(args: Array[String]) {
    import ConfigStorage._
    for (
      cfg <- read();
      god = new {
        val tz = TimeZone.getTimeZone(cfg.tz)
      }
        with FeedCreator with FetchStats with FeedParser with Login with DateReaders.RussianDateReader;
      session <- god.login(cfg.username, cfg.password);
      entries = god.fetch(session);
      lastFeedDate <- entries.headOption.map(_.date);
      feed <- god.createFeed(entries, cfg.lastDate.getOrElse(new Date(0)), cfg.tz)
    ) {
      println(feed)
      write(cfg.copy(lastDate = Some(lastFeedDate)))
    }
  }

}