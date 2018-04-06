package uk.pkerrigan

package object httpsecurityreporting {
  type DomainList = List[String]
  type CspFilterList = List[String]

  case class StatsConfig(server: String, port: Int)

  object StatsConfig {
    def fromOptionals(server: Option[String], port: Option[Int]): Option[StatsConfig] =
      for {
        server <- server
        port <- port
      } yield StatsConfig(server, port)
  }
}