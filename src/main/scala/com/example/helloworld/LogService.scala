package com.example.helloworld

import cats.effect._

class LogService {
  def log(message: String): IO[String] = {
    IO({
      println(message)
      message
    })
  }
}
