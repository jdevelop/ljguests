package com.jdevelop.ljguests

import java.util.{TimeZone, Locale, Date}
import java.text.SimpleDateFormat

/**
 * User: Eugene Dzhurinsky
 * Date: 3/1/13
 */
trait DateReader {

  val tz: TimeZone

  def readDate(dateStr: String): Option[Date]

}
