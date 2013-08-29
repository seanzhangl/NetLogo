// (C) Uri Wilensky. https://github.com/NetLogo/NetLogo

package org.nlogo.tortoise
package dock

class TestBasics extends DockingSuite {

  test("comments") { implicit fixture => import fixture._
    compare("3 ; comment")
    compare("[1 ; comment\n2]")
  }

  test("simple literals") { implicit fixture => import fixture._
    compare("false")
    compare("true")
    compare("2")
    compare("2.0")
    compare("\"foo\"")
  }

  test("literal lists") { implicit fixture => import fixture._
    compare("[]")
    compare("[1]")
    compare("[1 2]")
    compare("[\"foo\"]")
    compare("[1 \"foo\" 2]")
    compare("[1 [2] [3 4 [5]] 6 [] 7]")
    compare("[false true]")
  }

  test("arithmetic") { implicit fixture => import fixture._
    compare("2 + 2")
    compare("1 + 2 + 3")
    compare("1 - 2 - 3")
    compare("1 - (2 - 3)")
    compare("(1 - 2) - 3")
    compare("2 * 3 + 4 * 5")
    compare("6 / 2 + 12 / 6")
  }

  test("equality") { implicit fixture => import fixture._
    compare("5 = 5")
    compare(""""hello" = "hello"""")
  }

  test("empty commands") { implicit fixture => import fixture._
    testCommand("")
  }

  test("rng") { implicit fixture => import fixture._
    testCommand("random-seed 0 output-print random 100000")
  }

  test("printing") { implicit fixture => import fixture._
    testCommand("output-print 1")
    testCommand("output-print \"foo\"")
    testCommand("output-print 2 + 2")
    testCommand("output-print 1 output-print 2 output-print 3")
  }

  test("let") { implicit fixture => import fixture._
    testCommand("let x 5  output-print x")
  }

  test("globals: set") { implicit fixture => import fixture._
    declare("globals [x] to foo [i] set x i output-print x end")
    testCommand("foo 5 foo 6 foo 7")
  }

  test("clear-all clears globals") { implicit fixture => import fixture._
    declare("globals [g1 g2]")
    testCommand("set g1 88 set g2 99")
    testCommand("output-print (word g1 g2)")
    testCommand("clear-all")
    testCommand("output-print (word g1 g2)")
  }

}
