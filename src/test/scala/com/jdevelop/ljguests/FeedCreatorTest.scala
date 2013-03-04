package com.jdevelop.ljguests

import org.scalatest.FlatSpec
import org.junit.runner.RunWith
import org.scalatest.junit.JUnitRunner
import java.text.SimpleDateFormat
import xml.Elem
import java.util.TimeZone

/**
 * User: Eugene Dzhurinsky
 * Date: 3/2/13
 */
@RunWith(classOf[JUnitRunner])
class FeedCreatorTest extends FlatSpec {

  "FeedCreator" should "generate feed correctly" in {

    val sdf = {
      val z = new SimpleDateFormat("dd.MM.yyyy HH:mm")
      z.setTimeZone(TimeZone.getTimeZone("EST"))
      z
    }

    implicit def strToDate(dateStr: String) = sdf.parse(dateStr)

    val samples = List(
      Entry("maxal", "02.03.2013 22:52"),
      Entry("bializ", "02.03.2013 18:10"),
      Entry("mpd", "02.03.2013 08:11"),
      Entry("nobodyzz", "28.02.2013 08:11"),
      Entry("self", "28.02.2013 08:11"),
      Entry("self", "27.02.2013 08:11")
    )

    val feed = new FeedCreator {}.createFeed(samples, "29.02.2013 00:00", "EST")
    assert(feed isDefined)
    (feed.get \\ "item" \ "pubDate").map(_.text.trim).zip(
      List("Sat, 02 Mar 2013 22:52 -0500", "Sat, 02 Mar 2013 18:10 -0500", "Sat, 02 Mar 2013 08:11 -0500")
    ).foreach {
      case (l, r) => assert(l === r)
    }
  }


}
