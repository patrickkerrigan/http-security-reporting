package uk.pkerrigan.httpsecurityreporting.entity

import akka.http.scaladsl.marshallers.sprayjson.SprayJsonSupport
import spray.json._

final case class XssReport(
                            requestUrl: String,
                            requestBody: String
                          )

trait XssReportJson extends SprayJsonSupport {

  implicit object XssReportJsonFormat extends RootJsonFormat[XssReport] {
    def write(p: XssReport) = JsObject()

    def read(value: JsValue): XssReport = {
      value.asJsObject.getFields("xss-report") match {
        case Seq(csp@JsObject(_)) => readInner(csp)
        case _ => throw DeserializationException("Invalid XSS report")
      }
    }

    def readInner(value: JsObject): XssReport = {
      value.getFields(
        "request-url",
        "request-body"
      ) match {
        case Seq(JsString(requestUrl), JsString(requestBody)) =>
          XssReport(requestUrl, requestBody)
        case _ =>
          throw DeserializationException("Invalid XSS report")
      }
    }

  }
}