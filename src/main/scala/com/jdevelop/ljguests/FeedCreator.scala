package com.jdevelop.ljguests

import java.text.SimpleDateFormat
import java.util.Date

/**
 * User: Eugene Dzhurinsky
 * Date: 3/2/13
 */
trait FeedCreator {

  def createFeed(entries: Iterable[Entry], startDate: Date) = {
    val feedFormat = new SimpleDateFormat("E, dd MMM yyyy HH:mm Z")
    val feedEntries = entries.filter {
      case Entry(_, entryDate) => entryDate.compareTo(startDate) > 0
    }
    val lastItemDate = feedFormat.format(feedEntries.head.date)
    <rss version="2.0">
      <channel>
        <title>LJ Guests feed</title>
        <description>Guests feed
          {lastItemDate}
        </description>
        <link>http://github.com/jdevelop/ljguests</link>
        <lastBuildDate>
          {lastItemDate}
        </lastBuildDate>
        <pubDate>
          {lastItemDate}
        </pubDate>{for (Entry(user, visitDate) <- feedEntries) yield {
        <item>
          <title>
            {user}
          </title>
          <description>
            {user}
          </description>
          <pubDate>
            {feedFormat.format(visitDate)}
          </pubDate>
        </item>
      }}
      </channel>
    </rss>
  }

}