package uk.pkerrigan.httpsecurityreporting.csp.usecase

import java.net.{DatagramPacket, DatagramSocket, InetAddress, URL}

import uk.pkerrigan.httpsecurityreporting._
import uk.pkerrigan.httpsecurityreporting.entity.CspReport

import scala.util.Try

class ProcessCspViolation(val domains: DomainList, val filter: CspFilterList, val statsServer: Option[StatsConfig]) {
  val socket: DatagramSocket = new DatagramSocket

  def apply(report: CspReport): Unit =
    Try {
      new URL(report.documentUri)
    }.toOption match {
      case Some(validUrl) if shouldAcceptReport(report, validUrl.getHost) => processReport(report, validUrl.getHost)
      case _ =>
    }


  private def processReport(report: CspReport, host: String): Unit = {
    sendToStatsd(s"http-security.csp.$host.${report.effectiveDirective}:1|c")
    println(s"[CSP] [$host] [${report.effectiveDirective}] Loading ${report.blockedUri} from ${report.documentUri} blocked")
  }

  private def sendToStatsd(test: String): Unit =
    statsServer.foreach { config =>
      socket.send(new DatagramPacket(test.getBytes, test.getBytes.length, InetAddress.getByName(config.server), config.port))
    }

  private def shouldAcceptReport(report: CspReport, host: String): Boolean =
    domains.contains(host) &&
    !filter.contains(report.blockedUri)

}
