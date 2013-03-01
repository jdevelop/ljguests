package com.jdevelop.ljguests

import akka.actor.{Props, ActorSystem, Actor}
import java.util.concurrent.CountDownLatch
import org.apache.http.client.methods.HttpGet
import org.jsoup.Jsoup
import org.jsoup.nodes.Document
import org.jsoup.select.Elements

/**
 * User: Eugene Dzhurinsky
 * Date: 2/28/13
 */

object FetchStats {

  private val PAGES = 25

}

trait FetchStats {

  import FetchStats.PAGES

  private var counter: CountDownLatch = _

  def actorSystem: ActorSystem

  val fetchActor = actorSystem.actorOf(Props(classOf[FetchActor]))

  val collectActor = actorSystem.actorOf(Props(classOf[FetchActor]))

  private abstract sealed class Message

  private case class Fetch(url: String) extends Message

  private case class Reply(entries: Iterable[Entry])

  private class FetchActor extends Actor {

    override def receive = {
      case Fetch(url) =>
        val response = client.execute(new HttpGet(url))
    }

  }

  private class CollectActor extends Actor {

    override def receive = {
      case Reply(entries) =>
        counter.countDown()
    }

  }

  def fetch(session: Session) {
    counter = new CountDownLatch(PAGES)
    client.getCookieStore.clear()
    session.cookie.foreach(client.getCookieStore.addCookie(_))
    new CountDownLatch(PAGES)
    (1 to PAGES).foreach {
      case page => fetchActor ! Fetch("http://www.livejournal.com/statistics/guests/?page=" + page)
    }
    counter.await()
  }

}