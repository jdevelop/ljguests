package com.jdevelop.ljguests

import java.util.concurrent._
import org.apache.http.client.methods.HttpGet
import com.jdevelop.ljguests.DateReaders.RussianDateReader
import java.io.InputStream
import concurrent.{Await, ExecutionContext, Future}
import concurrent.duration.Duration
import java.util.TimeZone

/**
 * User: Eugene Dzhurinsky
 * Date: 2/28/13
 */

object FetchStats {

  private val PAGES = 25

}

trait FetchStats {

  this: FeedParser =>

  val tz: TimeZone

  import FetchStats.PAGES

  import concurrent.future
  import collection.mutable.PriorityQueue

  def fetch(session: Session): Iterable[Entry] = {
    val pool: ExecutorService = Executors.newCachedThreadPool()
    implicit val ec = ExecutionContext.fromExecutor(pool)
    client.getCookieStore.clear()
    session.cookie.foreach(client.getCookieStore.addCookie(_))
    val f = Future.sequence(
      (1 to PAGES).map(
        page => future {
          val parser = new {
            val tz = this.tz
          } with FeedParser with RussianDateReader
          val url: String = "http://www.livejournal.com/statistics/guests/?page=" + page
          val response = client.execute(new HttpGet(url))
          val contentStream: InputStream = response.getEntity.getContent
          val entries = parser.parse(contentStream, url)
          contentStream.close()
          entries
        }
      )
    )
    val entries = Await.result(f, Duration(30, TimeUnit.SECONDS))

    implicit val order = new Ordering[Entry] {
      def compare(x: Entry, y: Entry): Int = x.date.compareTo(y.date)
    }

    val q = new PriorityQueue()
    entries.flatten.map(q.+=(_))
    pool.shutdown()
    q.toIterable
  }

}