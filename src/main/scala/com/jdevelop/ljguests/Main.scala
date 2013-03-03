package com.jdevelop.ljguests

import java.util.Date
import java.io.{Writer, FileWriter, File}
import util.parsing.json.{JSONObject, JSON}
import java.text.SimpleDateFormat

/**
 * User: Eugene Dzhurinsky
 * Date: 3/2/13
 */
object Main {

  private case class Config(username: String, password: String, lastDate: Option[Date])

  private class ClassCaster[T] {
    def unapply(a: Any): Option[T] = Some(a.asInstanceOf[T])
  }

  private object AsString extends ClassCaster[String]

  private object ConfigStorage {

    def sdf = new SimpleDateFormat("dd-MM-yyyy")

    val home = new File(sys.env("HOME"))

    val confFile = new File(home, ".ljguests")

    def read(): Option[Config] = {
      require(confFile.exists(), "Configuration file '" + confFile + "' does not exist")
      JSON.parseFull(io.Source.fromFile(confFile).mkString) flatMap {
        case objMap: Map[String, Any] =>
          for (
            AsString(uname) <- objMap.get("username");
            AsString(passwd) <- objMap.get("password")
          ) yield Config(uname,
            passwd,
            objMap.get("lastDate").map((x: Any) => sdf.parse(x.toString))
          )
        case _ => None
      }
    }

    def write(cfg: Config) {
      val mainData: Map[String, Any] = Map(
        "username" -> cfg.username,
        "password" -> cfg.password
      )
      val content = new JSONObject(
        cfg.lastDate.map((z: Date) => mainData + ("lastDate" -> sdf.format(z))).getOrElse(mainData)
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
    val god = new FeedCreator with FetchStats with FeedParser with Login with DateReaders.RussianDateReader
    for (
      cfg <- read();
      session <- god.login(cfg.username, cfg.password);
      entries = god.fetch(session);
      lastFeedDate <- entries.headOption.map(_.date)
    ) {
      println(god.createFeed(entries, cfg.lastDate.getOrElse(new Date(0))))
      write(cfg.copy(lastDate = Some(lastFeedDate)))
    }
  }

}