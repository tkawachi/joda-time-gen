package com.github.tkawachi.jodatimegen

import org.scalacheck.{Gen, Prop, Properties}
import IsoJodaTimeGen._
import Prop.forAll

class IsoJodaTimeGenCheck extends Properties("IsoJodaTimeGen") {
  // Able to generate values without throwing exceptions
  private def canGen[A](gen: Gen[A]): Prop = Prop.secure(forAll(gen)(_ => true ))

  include(new Properties("Random") {
    property("dateTimeZone") = canGen(Random.dateTimeZone)
    property("utcDateTime") = canGen(Random.utcDateTime)
    property("dateTime") = canGen(Random.dateTime)
    property("localDateTime") = canGen(Random.localDateTime)
    property("localTime") = canGen(Random.localTime)
    property("interval") = canGen(Random.interval)
    property("duration") = canGen(Random.duration)
    property("period") = canGen(Random.period)
  })

  property("dateTimeZone") = canGen(dateTimeZone)
  property("utcDateTime") = canGen(utcDateTime)
  property("dateTime") = canGen(dateTime)
  property("localDateTime") = canGen(localDateTime)
  property("localTime") = canGen(localTime)
  property("interval") = canGen(interval)
  property("duration") = canGen(duration)
  property("period") = canGen(period)
}
