package com.jdevelop

import org.apache.http.impl.client.DefaultHttpClient
import org.apache.http.impl.conn.PoolingClientConnectionManager

/**
 * User: Eugene Dzhurinsky
 * Date: 2/28/13
 */
package object ljguests {

  val client = new DefaultHttpClient(new PoolingClientConnectionManager())

}
