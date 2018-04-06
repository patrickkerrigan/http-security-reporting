package uk.pkerrigan.httpsecurityreporting.action

import akka.http.scaladsl.model.{ContentTypes, StatusCodes}
import akka.http.scaladsl.server.{Directives, Route}
import uk.pkerrigan.httpsecurityreporting.ActionTrait
import uk.pkerrigan.httpsecurityreporting.csp.usecase.ProcessXssAttack
import uk.pkerrigan.httpsecurityreporting.entity.{XssReport, XssReportJson}

class Xss(processor: ProcessXssAttack) extends ActionTrait with Directives with XssReportJson {
  def apply(): Route =
    path("xss") {
      post {
        mapRequest(_.mapEntity(_.withContentType(ContentTypes.`application/json`))) {
          entity(as[XssReport]) { report =>
            processor(report)
            complete(StatusCodes.NoContent)
          }
        }
      }
    }
}
