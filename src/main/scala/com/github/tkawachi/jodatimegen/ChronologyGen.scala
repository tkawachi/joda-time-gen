package com.github.tkawachi.jodatimegen

import org.joda.time.chrono._
import org.joda.time.{Chronology, DateTimeZone}
import org.scalacheck.Gen

trait ChronologyGen {

  def chronology(zone: DateTimeZone): Gen[Chronology] =
    Gen.oneOf(
      ISOChronology.getInstance(zone),
      GJChronology.getInstance(zone),
      GregorianChronology.getInstance(zone),
      JulianChronology.getInstance(zone),
      CopticChronology.getInstance(zone),
      BuddhistChronology.getInstance(zone),
      EthiopicChronology.getInstance(zone),
      IslamicChronology.getInstance(zone)
    )
}

object ChronologyGen extends ChronologyGen
