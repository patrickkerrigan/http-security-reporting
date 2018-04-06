package uk.pkerrigan.httpsecurityreporting.csp.usecase

import java.net.URL

import uk.pkerrigan.httpsecurityreporting._
import uk.pkerrigan.httpsecurityreporting.entity.XssReport

import scala.util.Try

class ProcessXssAttack(val domains: DomainList) {
  def apply(report: XssReport): Unit =
    Try {
      new URL(report.requestUrl)
    }.toOption match {
      case Some(validUrl) if domains.contains(validUrl.getHost) => processReport(report, validUrl.getHost)
      case _ =>
    }

  def processReport(report: XssReport, host: String): Unit = {
    println(s"[XSS] [$host] Blocked XSS attack")
  }
}
