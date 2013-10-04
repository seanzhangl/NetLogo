// (C) Uri Wilensky. https://github.com/NetLogo/NetLogo

package org.nlogo.tortoise
package dock

class TestBreeds extends DockingSuite {
  test("create breeds") { implicit fixture => import fixture._
    declare("""| breed [mice mouse]
               | breed [frogs frog]""".stripMargin)
    testCommand("output-print count frogs")
    testCommand("output-print count turtles")
    testCommand("create-turtles 1")
    testCommand("create-frogs 1")
    testCommand("output-print count frogs")
    testCommand("output-print count turtles")
  }

  test("is breed") { implicit fixture => import fixture._
    declare("""| breed [mice mouse]
               | breed [frogs frog]
               | globals [glob1]""".stripMargin)
    testCommand("create-turtles 1")
    testCommand("output-print is-frog? turtle 0")
    testCommand("create-frogs 1")
    testCommand("output-print is-frog? turtle 1")
    testCommand("output-print is-mouse? turtle 1")
    testCommand("set glob1 turtle 1")
    testCommand("output-print is-frog? glob1")
  }

  test("breed shapes") { implicit fixture => import fixture._
    declare("""| breed [frogs frog]""".stripMargin)
    testCommand("""set-default-shape turtles "sheep" """)
    testCommand("create-turtles 1")
    testCommand("""set-default-shape frogs "wolf" """)
    testCommand("create-frogs 1")
    testCommand("output-print [ shape ] of turtle 0")
    testCommand("output-print [ shape ] of turtle 1")
    testCommand("output-print [ shape ] of frog 0")
  }

  test("breed hatch") { implicit fixture => import fixture._
    declare("""| breed [frogs frog]""".stripMargin)
    testCommand("""set-default-shape turtles "sheep" """)
    testCommand("create-turtles 2")
    testCommand("""set-default-shape frogs "wolf" """)
    testCommand("create-frogs 2")
    testCommand("ask turtles [ hatch 3 ]")
    testCommand("ask frogs [ hatch 5 ]")
    testCommand("output-print count turtles")
    testCommand("output-print count frogs")
    testCommand("ask turtles [ hatch-frogs 3 ]")
    testCommand("output-print count turtles")
    testCommand("output-print count frogs")
  }

  test("hatch other variables") { implicit fixture => import fixture._
    declare("""| turtles-own [energy]""".stripMargin)
    testCommand("create-turtles 1")
    testCommand("ask turtles [ output-print energy ]")
    testCommand("ask turtles [ set energy 9 ]")
    testCommand("ask turtles [ output-print energy ]")
    testCommand("ask turtles [ hatch 1 ]")
    testCommand("ask turtles [ output-print energy ]")
  }

  test("breed here") { implicit fixture => import fixture._
    declare("""| breed [mice mouse]
               | breed [frogs frog]""".stripMargin)
    testCommand("create-frogs 2")
    testCommand("create-mice 2")
    testCommand("output-print count mice")
    testCommand("output-print count frogs")
    testCommand("ask frogs [ output-print count mice-here ]")
    testCommand("ask mice [ output-print count frogs-here ]")
    testCommand("ask mice [ output-print count mice-here ]")
    testCommand("ask mice [ fd 1 ]")
    testCommand("ask mice [ output-print count mice-here ]")
    testCommand("ask mice [ output-print count frogs-here ]")
  }
}
