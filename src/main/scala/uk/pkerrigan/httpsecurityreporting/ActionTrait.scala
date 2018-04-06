package uk.pkerrigan.httpsecurityreporting

import akka.http.scaladsl.server.Route

trait ActionTrait {
  def apply(): Route
}
