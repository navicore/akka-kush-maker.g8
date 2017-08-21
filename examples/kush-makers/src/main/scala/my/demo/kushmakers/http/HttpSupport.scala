package my.demo.kushmakers.http

import java.io.IOException

import akka.http.scaladsl.model._
import akka.http.scaladsl.model.headers.{HttpOrigin, HttpOriginRange}
import akka.http.scaladsl.server.Directives._
import akka.http.scaladsl.server.{Directive, ExceptionHandler, RejectionHandler}
import ch.megard.akka.http.cors.scaladsl.CorsDirectives._
import ch.megard.akka.http.cors.scaladsl.settings.CorsSettings
import com.typesafe.config.{Config, ConfigFactory}
import com.typesafe.scalalogging.LazyLogging

/**
  * cors and error handling for http
  */
trait HttpSupport extends LazyLogging {

  val conf: Config = ConfigFactory.load()
  val corsOrigin: String = conf.getString("main.corsOrigin")
  logger.info(s"corsOrigin is $corsOrigin")
  val urlpath: String = conf.getString("main.path")
  logger.info(s"urlPath is $urlpath")
  val port: Int = conf.getInt("main.port")

  val corsSettings: CorsSettings.Default = CorsSettings.defaultSettings.copy(
    allowedOrigins = corsOrigin match {
      case "*" => HttpOriginRange.*
      case _ => HttpOriginRange(HttpOrigin(corsOrigin))
    }
  )

  val rejectionHandler
    : RejectionHandler = corsRejectionHandler withFallback RejectionHandler.default

  val handleErrors
    : Directive[Unit] = handleRejections(rejectionHandler) & handleExceptions(
    ExceptionHandler {
      case e: akka.actor.InvalidActorNameException =>
        logger.warn(s"$e")
        complete(StatusCodes.BadRequest -> e.getMessage)
      case e: NoSuchElementException =>
        logger.warn(s"$e")
        complete(StatusCodes.NotFound -> e.getMessage)
      case e: IllegalArgumentException =>
        logger.warn(s"$e")
        complete(StatusCodes.BadRequest -> e.getMessage)
      case e: IOException =>
        logger.warn(s"$e")
        complete(StatusCodes.Forbidden -> e.getMessage)
    }
  )

}
