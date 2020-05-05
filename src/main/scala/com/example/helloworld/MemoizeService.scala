package com.example.helloworld

import cats.effect._

import scala.concurrent.duration._
import scalacache._
import scalacache.caffeine._
import scalacache.memoization._
import scalacache.Mode

class MemoizeService {
  implicit val cache: Cache[String] = CaffeineCache[String]
  implicit val mode: Mode[IO] = scalacache.CatsEffect.modes.async

  // memoize automatically generates keys that won't conflict
  // no need to define custom cache keys to differentiate uppercase and lowercase

  def uppercase(message: String): IO[String] = memoizeF[IO, String](Some(1.second)) (IO({
    val upper = message.toUpperCase
    println(upper)
    upper
  }))

  def lowercase(message: String): IO[String] = memoizeF[IO, String](Some(1.second)) (IO({
    val lower = message.toLowerCase
    println(lower)
    lower
  }))
}
