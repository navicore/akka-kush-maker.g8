package my.demo.kushmakers

import akka.actor.ActorSystem
import akka.http.scaladsl.Http
import akka.http.scaladsl.model._
import akka.http.scaladsl.server.Directives._
import akka.stream.ActorMaterializer
import ch.megard.akka.http.cors.scaladsl.CorsDirectives._
import com.typesafe.scalalogging.LazyLogging

object Main extends LazyLogging with HttpSupport {

  def main(args: Array[String]) {

    implicit val system = ActorSystem("rest-system")
    implicit val materializer = ActorMaterializer()
    implicit val executionContext = system.dispatcher

    val route =
      path(urlpath) {
        logRequest(urlpath) {
          handleErrors {
            cors(corsSettings) {
              get {
                logger.debug(s"get $urlpath")
                complete(HttpEntity(ContentTypes.`application/json`,
                                    "{\"msg\": \"Say hello to akka-http\"}\n"))
              } ~
              post {
                logger.debug(s"post $urlpath")
                complete(HttpEntity(ContentTypes.`application/json`,
                                    "{\"msg\": \"Say whoa to akka-http\"}\n"))
              }
            }
          }
        }
      }

    Http().bindAndHandle(route, "0.0.0.0", port)
  }
}

