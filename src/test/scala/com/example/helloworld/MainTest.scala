package com.example.helloworld

import io.finch._
import org.scalatest.FunSuite

class MainTest extends FunSuite {
  test("healthcheck") {
    assert(Main.healthcheck(Input.get("/")).awaitValueUnsafe() == Some("OK"))
  }

  test("helloWorld") {
    assert(Main.helloWorld(Input.get("/hello")).awaitValueUnsafe() == Some(Main.Message("World")))
  }

  test("hello") {
    assert(Main.hello(Input.get("/hello/foo")).awaitValueUnsafe() == Some(Main.Message("foo")))
  }

  test("log") {
    assert(Main.log(Input.get("/log/message")).awaitValueUnsafe() == Some("message"))
  }

  test("uppercase") {
    assert(Main.uppercase(Input.get("/uppercase/upper")).awaitValueUnsafe() == Some("UPPER"))
  }

  test("lowercase") {
    assert(Main.lowercase(Input.get("/lowercase/LOWER")).awaitValueUnsafe() == Some("lower"))
  }
}
