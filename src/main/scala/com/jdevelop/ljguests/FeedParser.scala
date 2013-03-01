package com.jdevelop.ljguests

import java.io.InputStream
import org.jsoup.Jsoup
import org.jsoup.select.Elements

import collection.JavaConversions._
import org.jsoup.nodes.Element

/**
 * User: Eugene Dzhurinsky
 * Date: 2/28/13
 */
trait FeedParser {

  def parse(is: InputStream, url: String) = {
    val doc = Jsoup.parse(is, "UTF-8", url)
    val numbers: Elements = doc.select("tr > td.s-list-numb")
    numbers.map(_.parent()).flatMap {
      case e: Element =>
        val user = e.select("td.s-list-desc > span.ljuser.i-ljuser > a.i-ljuser-username > b").text()
        val dateStr = e.select("td.s-list-time").text()
        if (user == "") {
          List()
        } else {
          List((user, dateStr))
        }
    }
  }

}