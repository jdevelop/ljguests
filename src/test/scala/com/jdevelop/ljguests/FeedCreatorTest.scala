package com.jdevelop.ljguests

import org.scalatest.FlatSpec
import xml.Elem
import org.junit.runner.RunWith
import org.scalatest.junit.JUnitRunner
import java.text.SimpleDateFormat

/**
 * User: Eugene Dzhurinsky
 * Date: 3/2/13
 */
@RunWith(classOf[JUnitRunner])
class FeedCreatorTest extends FlatSpec {

  "FeedCreator" should "generate feed correctly" in {

    val sdf = new SimpleDateFormat("dd.MM.yyyy HH:mm")

    implicit def strToDate(dateStr: String) = sdf.parse(dateStr)

    val samples = List(
      Entry("maxal", "02.03.2013 22:52"),
      Entry("bializ", "02.03.2013 18:10"),
      Entry("mpd", "02.03.2013 08:11"),
      Entry("nobodyzz", "28.02.2013 08:11"),
      Entry("self", "28.02.2013 08:11"),
      Entry("self", "27.02.2013 08:11")
    )

    val feed: Map[String, Elem] = new FeedCreator {}.createFeed(samples)
    assert(3 === feed.size, "Keys are " + feed.keys)
    for ((date, expected) <- List(
      "02-03-2013" -> 1,
      "28-02-2013" -> 1,
      "27-02-2013" -> 1
    )) {
      val z = feed.get(date)
      assert(z.isDefined, "Key is missing : " + date + " => " + feed.keys)
      assert(expected === z.get.length, "Wrong number of entries => " + date)
    }
  }


}
