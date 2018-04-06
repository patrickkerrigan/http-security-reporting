package uk.pkerrigan.httpsecurityreporting

import akka.actor.ActorSystem
import akka.http.scaladsl.Http
import akka.http.scaladsl.server.Directives._
import akka.stream.ActorMaterializer
import com.typesafe.config.{Config, ConfigFactory}
import uk.pkerrigan.httpsecurityreporting.action.{Csp, Status, Xss}
import uk.pkerrigan.httpsecurityreporting.csp.usecase.{ProcessCspViolation, ProcessXssAttack}

import scala.concurrent.ExecutionContextExecutor
import scala.collection.JavaConverters._


object Main extends App with OptionalConfigOptions{

  implicit val system: ActorSystem = ActorSystem("httpsecurityreporting")
  implicit val materializer: ActorMaterializer = ActorMaterializer()
  implicit val executionContext: ExecutionContextExecutor = system.dispatcher

  val conf: Config = ConfigFactory.load

  val allowedDomains: DomainList = conf.getStringList("domains").asScala.toList
  val cspFilter: CspFilterList = conf.getStringList("cspfilter").asScala.toList

  val actions: Seq[ActionTrait] = Seq(
    new Status(),
    new Csp(new ProcessCspViolation(
      allowedDomains,
      cspFilter,
      StatsConfig.fromOptionals(conf.getOptionalString("stats.server"), conf.getOptionalInt("stats.port"))
    )),
    new Xss(new ProcessXssAttack(allowedDomains))
  )

  val routes = Security.withSecurityHeaders {
    pathPrefix(conf.getString("server.prefix")) {
      actions.foldLeft(Security.cors)((route, action) => route ~ action())
    }
  }

  val bindingFuture = Http().bindAndHandle(routes, conf.getString("server.host"), conf.getInt("server.port"))

}
