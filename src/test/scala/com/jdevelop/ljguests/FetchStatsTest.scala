package com.jdevelop.ljguests

import org.scalatest.FlatSpec
import org.junit.runner.RunWith
import org.scalatest.junit.JUnitRunner
import java.util.TimeZone

/**
 * User: Eugene Dzhurinsky
 * Date: 3/2/13
 */
@RunWith(classOf[JUnitRunner])
class FetchStatsTest extends FlatSpec with FetchStats with FeedParser with DateReaders.RussianDateReader {

  val tz = TimeZone.getTimeZone("EST")

  ignore should "fetch data in order" in {
    val data: Option[Iterable[Entry]] = (for (
      session <- new Login() {}.login("username", "passwd");
      feed = fetch(session)
    ) yield feed)
    assert(data isDefined)
    val dataVal = data.get
    dataVal.view.zip(dataVal.tail.view).foreach {
      case (l, r) => l.date.compareTo(r.date) < 0
    }
  }

}
