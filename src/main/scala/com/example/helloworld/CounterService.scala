package com.example.helloworld

import cats.effect._
import cats.effect.concurrent._

object CounterService {
  val counter = Ref.of[IO, Int](0)

  def inc(c: Ref[IO, Int]): IO[Int] = {
    c.modify(a => (a + 1, a))
  }
}
