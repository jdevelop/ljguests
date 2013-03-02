package com.jdevelop.ljguests

import org.scalatest.FlatSpec
import org.junit.runner.RunWith
import org.scalatest.junit.JUnitRunner

/**
 * User: Eugene Dzhurinsky
 * Date: 3/1/13
 */
@RunWith(classOf[JUnitRunner])
class FeedParserTest extends FlatSpec with FeedParser with DateReaders.RussianDateReader {

  "FeedParser" should "extract all records" in {
    val res = parse(classOf[FeedParserTest].getResourceAsStream("/view.htm"), "http://123.com")
    assert(11 === res.size)
    res.zip(List("fantaseour", "reincarnat", "xeno_by", "theiced", "dmzlj", "dvig_al", "thekonst", "rigidus",
      "izard", "si14", "civil696")).foreach {
      case (Entry(uname, date), expected) =>
        assert(expected === uname, "wrong username")
        assert(date.getTime != 0, "Date is not parsed")
    }
  }

}