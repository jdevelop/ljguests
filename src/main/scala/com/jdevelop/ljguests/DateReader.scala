package com.jdevelop.ljguests

import java.util.{Locale, Date}
import java.text.SimpleDateFormat

/**
 * User: Eugene Dzhurinsky
 * Date: 3/1/13
 */
trait DateReader {

  def readDate(dateStr: String): Option[Date]

}
