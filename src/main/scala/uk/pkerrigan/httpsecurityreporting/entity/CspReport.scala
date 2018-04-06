package uk.pkerrigan.httpsecurityreporting.entity

import akka.http.scaladsl.marshallers.sprayjson.SprayJsonSupport
import spray.json._

import scala.util.Try

final case class CspReport(
  documentUri: String,
  blockedUri: String,
  effectiveDirective: String,
)

trait CspReportJson extends SprayJsonSupport {

  implicit object CspReportJsonFormat extends RootJsonFormat[CspReport] {
    def write(p: CspReport) = JsObject()

    def read(value: JsValue): CspReport = {
      value.asJsObject.getFields("csp-report") match {
        case Seq(csp@JsObject(_)) => readInner(csp)
        case _ => throw DeserializationException("Invalid CSP report")
      }
    }

    def readInner(value: JsObject): CspReport = {
      {Try {readInnerV3(value)} orElse Try {readInnerV2(value)}}.toOption match {
        case Some(cspReport) => cspReport
        case _ => throw DeserializationException("Invalid CSP report")
      }
    }

    def readInnerV3(value: JsObject): CspReport = {
      value.getFields(
        "document-uri",
        "blocked-uri",
        "effective-directive"
      ) match {
        case Seq(JsString(documentUri), JsString(blockedUri), JsString(violatedDirective)) =>
          CspReport(documentUri, blockedUri, violatedDirective)
        case _ =>
          throw DeserializationException("Invalid CSP report")
      }
    }

    def readInnerV2(value: JsObject): CspReport = {
      value.getFields(
        "document-uri",
        "blocked-uri",
        "violated-directive"
      ) match {
        case Seq(JsString(documentUri), JsString(blockedUri), JsString(violatedDirective)) =>
          CspReport(documentUri, blockedUri, violatedDirective.split(" ").head)
        case _ =>
          throw DeserializationException("Invalid CSP report")
      }
    }
  }
}