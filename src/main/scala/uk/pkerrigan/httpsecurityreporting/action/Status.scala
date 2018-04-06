package uk.pkerrigan.httpsecurityreporting.action

import akka.http.scaladsl.model.StatusCodes
import akka.http.scaladsl.server.Directives._
import akka.http.scaladsl.server.Route
import uk.pkerrigan.httpsecurityreporting.ActionTrait

class Status extends ActionTrait {
  def apply(): Route =
    path("status") {
      get {
        complete(StatusCodes.OK, "OK")
      }
    }
}
