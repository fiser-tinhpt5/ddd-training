package model.issue

import scala.util.Try

/**
 * Created by septechuser on 24/11/2016.
 */
sealed abstract class Status(val status: String)

object Status {
  case object PENDING extends Status("PENDING")
  case object RESOLVED extends Status("RESOLVED")
  case object CANCELLED extends Status("CANCELLED")

  def fromString(str: String): Try[Status] = Try {
    str match {
      case "PENDING"   => PENDING
      case "RESOLVED"  => RESOLVED
      case "CANCELLED" => CANCELLED
      case _           => throw new IllegalArgumentException(s"${str} is not defined as Status")
    }
  }
}
