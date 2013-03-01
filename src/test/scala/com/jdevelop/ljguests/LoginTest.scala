package com.jdevelop.ljguests

import org.scalatest.FlatSpec
import org.junit.runner.RunWith
import org.scalatest.junit.JUnitRunner

/**
 * User: Eugene Dzhurinsky
 * Date: 2/28/13
 */
@RunWith(classOf[JUnitRunner])
class LoginTest extends FlatSpec with Login {

  ignore should "login user" in {
    assert(login("testusr", "testpass").isDefined)
  }

}
