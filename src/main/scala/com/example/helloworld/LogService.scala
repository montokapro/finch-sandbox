package com.example.helloworld

import cats.effect._

object LogService {
  def log(message: String): IO[String] = {
    IO({
      println(message)
      message
    })
  }
}
