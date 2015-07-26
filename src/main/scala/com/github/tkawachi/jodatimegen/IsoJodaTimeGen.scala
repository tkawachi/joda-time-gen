package com.github.tkawachi.jodatimegen

import org.joda.time._
import org.scalacheck.Gen
import org.scalacheck.Arbitrary.arbitrary

import scala.collection.JavaConverters._

/**
 * Randomly generated ISOChronology values
 */
trait IsoJodaTimeGen {

  /**
   * Randomly generated values
   */
  object Random {
    def dateTimeZone: Gen[DateTimeZone] =
      Gen.oneOf(DateTimeZone.getAvailableIDs.asScala.toSeq).map(DateTimeZone.forID)

    def utcDateTime = Gen.choose(
      DateTime.parse("1970-01-01T00:00:00Z").getMillis,
      DateTime.parse("3000-01-01T00:00:00Z").getMillis
    ).map(new DateTime(_, DateTimeZone.UTC))

    def dateTime = for {
      utc <- utcDateTime
      zone <- dateTimeZone
    } yield utc.withZone(zone)

    def localTime: Gen[LocalTime] = utcDateTime.map(_.toLocalTime)

    def localDateTime: Gen[LocalDateTime] = utcDateTime.map(_.toLocalDateTime)

    def interval: Gen[Interval] = for {
      dt1 <- dateTime
      dt2 <- dateTime
    } yield if (dt2.isAfter(dt1)) new Interval(dt1, dt2) else new Interval(dt2, dt1)

    def duration: Gen[Duration] = arbitrary[Long].map(new Duration(_))

    def period: Gen[Period] = for {
      years <- arbitrary[Int]
      months <- arbitrary[Int]
      weeks <- arbitrary[Int]
      days <- arbitrary[Int]
      hours <- arbitrary[Int]
      minutes <- arbitrary[Int]
      seconds <- arbitrary[Int]
      millis <- arbitrary[Int]
    } yield Period.years(years).withMonths(months).withWeeks(weeks).withDays(days)
        .withHours(hours).withMinutes(minutes).withSeconds(seconds).withMillis(millis)
  }

  private def freq[A](general: Gen[A], specials: Seq[Gen[A]]): Gen[A] =
    Gen.frequency(specials.map((1, _)) :+(specials.length, general): _*)

  def dateTimeZone: Gen[DateTimeZone] =
    freq(Random.dateTimeZone, Seq(Gen.const(DateTimeZone.UTC)))

  def localTime: Gen[LocalTime] = {
    val specials = Seq(
      "00:00:00.000",
      "11:59:59.999",
      "12:00:00.000",
      "23:59:59.999"
    )

    freq(Random.localTime, specials.map(s => Gen.const(LocalTime.parse(s))))
  }

  def localDateTime: Gen[LocalDateTime] = utcDateTime.map(_.toLocalDateTime)

  def utcDateTime: Gen[DateTime] = {
    val specials = Seq(
      "1970-01-01T00:00:00.000Z",
      "1999-12-31T23:59:59.999Z", // end of 20th century
      "2000-01-01T00:00:00.000Z", // start of 21th century
      "2004-02-29T00:00:00.000Z", // Leap year
      "2004-02-29T23:59:59.999Z",
      "2100-02-28T23:59:59.999Z", // Not a leap year
      "2100-03-01T00:00:00.000Z",
      "2000-02-29T00:00:00.000Z", // Leap year
      "2000-02-29T23:59:59.999Z"
    )

    freq(Random.utcDateTime, specials.map(s => Gen.const(DateTime.parse(s))))
  }

  def dateTime: Gen[DateTime] = for {
    utc <- utcDateTime
    zone <- dateTimeZone
  } yield utc.withZoneRetainFields(zone)

  def interval: Gen[Interval] = freq(Random.interval, Seq(dateTime.map(dt => new Interval(dt, dt))))

  def duration: Gen[Duration] = Random.duration

  def period: Gen[Period] = freq(Random.period, Seq(Gen.const(Period.ZERO)))
}

object IsoJodaTimeGen extends IsoJodaTimeGen
