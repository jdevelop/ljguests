package com.jdevelop.ljguests

import java.util.{Calendar, GregorianCalendar, Locale, Date}
import java.text.SimpleDateFormat
import java.util.regex.{Matcher, Pattern}

/**
 * User: Eugene Dzhurinsky
 * Date: 3/1/13
 */
object DateReaders {

  private val rusDatePattern = Pattern.compile("(\\d+) ([^\\s]+) (\\d+), (\\d+):(\\d+)")

  private val rusMonths = List("января", "февраля", "марта", "апреля", "мая", "июня",
    "июля", "августа", "сентября", "октября", "ноября", "декабря").zipWithIndex.toMap

  trait SimpleDateReader extends DateReader {
    def readDate(dateStr: String): Option[Date] =
      Some(new SimpleDateFormat("dd MMMM, HH:mm", new Locale("ru", "RU")).parse(dateStr))
  }

  trait RussianDateReader extends DateReader {
    def readDate(dateStr: String): Option[Date] = {
      val matcher: Matcher = rusDatePattern.matcher(dateStr)
      if (matcher.find()) {
        val c = new GregorianCalendar
        c.set(Calendar.DATE, matcher.group(1).toInt)
        c.set(Calendar.MONTH, rusMonths.getOrElse(matcher.group(2), 0))
        c.set(Calendar.YEAR, matcher.group(3).toInt)
        c.set(Calendar.HOUR, matcher.group(4).toInt)
        c.set(Calendar.MINUTE, matcher.group(5).toInt)
        Some(c.getTime)
      } else {
        println(dateStr)
        None
      }
    }
  }

}
