package com.jdevelop.ljguests

import org.apache.http.client.methods.HttpPost
import org.apache.http.client.entity.UrlEncodedFormEntity
import org.apache.http.NameValuePair

import org.apache.http.message.BasicNameValuePair

import collection.JavaConversions._

/**
 * User: Eugene Dzhurinsky
 * Date: 2/28/13
 */

trait Login {

  def login(username: String, password: String): Option[Session] = {
    val post: HttpPost = new HttpPost("https://www.livejournal.com/login.bml")
    val params: java.util.List[NameValuePair] = List("user" -> username, "password" -> password).map {
      case (n, v) => new BasicNameValuePair(n, v)
    }
    post.setEntity(new UrlEncodedFormEntity(params))
    client.execute(post)
    client.getCookieStore.getCookies match {
      case lst if lst == null || lst.isEmpty || lst.find(_.getName == "ljsession").isEmpty => None
      case c => Some(Session(c.toList))
    }
  }

}