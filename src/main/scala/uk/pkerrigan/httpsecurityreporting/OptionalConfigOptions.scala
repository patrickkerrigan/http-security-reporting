package uk.pkerrigan.httpsecurityreporting

import com.typesafe.config.Config

trait OptionalConfigOptions {
  implicit class OptionalConfig(val config: Config) {

    def getOptionalString(path: String): Option[String] = getOptional[String](path, config.getString)
    def getOptionalInt(path: String): Option[Int] = getOptional[Int](path, config.getInt)

    private def getOptional[T](path: String, accessor: String => T): Option[T] =
      if (config.hasPath(path)) Some(accessor(path)) else None
  }
}
