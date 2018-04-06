package uk.pkerrigan.httpsecurityreporting

import akka.http.scaladsl.model.StatusCodes
import akka.http.scaladsl.model.headers.RawHeader
import akka.http.scaladsl.server.Directives._
import akka.http.scaladsl.server.Route

object Security {

  def withSecurityHeaders(route: Route): Route =
    respondWithHeaders(
      RawHeader("X-Content-Type-Options", "nosniff"),
      RawHeader("Strict-Transport-Security", "max-age=15552000")
    ) {
      route
    }

  def cors: Route =
    respondWithHeaders(
      RawHeader("Access-Control-Allow-Methods", "POST, OPTIONS")
    ) {
      options {
        complete(StatusCodes.OK)
      }
    }
}
