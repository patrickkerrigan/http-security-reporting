package uk.pkerrigan.httpsecurityreporting.action

import akka.http.scaladsl.model.{ContentTypes, StatusCodes}
import akka.http.scaladsl.server.Route
import akka.http.scaladsl.server.Directives
import uk.pkerrigan.httpsecurityreporting.ActionTrait
import uk.pkerrigan.httpsecurityreporting.csp.usecase.ProcessCspViolation
import uk.pkerrigan.httpsecurityreporting.entity.{CspReport, CspReportJson}

class Csp(processor: ProcessCspViolation) extends ActionTrait with Directives with CspReportJson {
  def apply(): Route =
    path("csp") {
      post {
        mapRequest(_.mapEntity(_.withContentType(ContentTypes.`application/json`))) {
          entity(as[CspReport]) { report =>
            processor(report)
            complete(StatusCodes.NoContent)
          }
        }
      }
    }
}
