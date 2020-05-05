package com.example.helloworld

import cats.effect.IO
import cats.effect.concurrent.Ref
import com.twitter.finagle.{Http, Service}
import com.twitter.finagle.http.{Request, Response}
import com.twitter.util.Await
import io.finch._
import io.finch.catsEffect._
import io.finch.circe._
import io.circe.generic.auto._
import com.twitter.util.Memoize

object Main extends App {
  val logService = new LogService
  val counterService = new CounterService
  val memoizeService = new MemoizeService

  case class Message(hello: String)

  def healthcheck: Endpoint[IO, String] = get(pathEmpty) {
    Ok("OK")
  }

  def helloWorld: Endpoint[IO, Message] = get("hello") {
    Ok(Message("World"))
  }

  def hello: Endpoint[IO, Message] = get("hello" :: path[String]) { s: String =>
    Ok(Message(s))
  }

  def log: Endpoint[IO, String] = get("log" :: path[String]) { s: String =>
    logService.log(s).map(Ok)
  }

  def uppercase: Endpoint[IO, String] = get("uppercase" :: path[String]) { s: String =>
    memoizeService.uppercase(s).map(Ok)
  }

  def lowercase: Endpoint[IO, String] = get("lowercase" :: path[String]) { s: String =>
    memoizeService.lowercase(s).map(Ok)
  }

  def inc(counter: Ref[IO, Int]): Endpoint[IO, String] = get("inc") {
    counterService.inc(counter).map(_.toString).map(Ok)
  }

  val server = for {
    counter <- counterService.counter
  } yield {
    val service = Bootstrap
      .serve[Text.Plain](healthcheck :+: log :+: uppercase :+: lowercase :+: inc(counter))
      .serve[Application.Json](helloWorld :+: hello)
      .toService

    Http.server.serve(":8081", service)
  }

  val handle = server.unsafeRunSync()

  Await.ready(handle)
}
