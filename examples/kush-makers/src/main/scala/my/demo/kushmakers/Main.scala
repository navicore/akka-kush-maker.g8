package my.demo.kushmakers

import akka.actor._
import akka.http.scaladsl.Http
import akka.http.scaladsl.model._
import akka.http.scaladsl.server.Directives._
import akka.pattern.ask
import akka.stream.ActorMaterializer
import akka.util.Timeout
import ch.megard.akka.http.cors.scaladsl.CorsDirectives._
import com.typesafe.scalalogging.LazyLogging
import my.demo.kushmakers.actors.FleetManager
import my.demo.kushmakers.entities._
import my.demo.kushmakers.http.HttpSupport
import spray.json._

import scala.concurrent.duration._
import scala.concurrent.{Await, ExecutionContextExecutor}

object Main extends LazyLogging with HttpSupport with JsonSupport {

  def main(args: Array[String]) {

    implicit val system: ActorSystem = ActorSystem("rest-system")
    implicit val materializer: ActorMaterializer = ActorMaterializer()
    implicit val executionContext: ExecutionContextExecutor = system.dispatcher
    implicit val timeout: Timeout = Timeout(5 seconds)

    val fleetManager = system.actorOf(FleetManager.props(), "fleet-manager")

    val route =
      pathPrefix(urlpath) {
        logRequest(urlpath) {
          handleErrors {
            cors(corsSettings) {
              path("fleet") {
                post {
                  decodeRequest { entity(as[FleetRequest]) { r =>
                    val f = fleetManager ? r
                    val command = Await.result(f, timeout.duration).asInstanceOf[FleetCommand]
                    complete(HttpEntity(ContentTypes.`application/json`, command.toJson.prettyPrint))
                  }}
                }
              }
            }
          }
        }
      }

    Http().bindAndHandle(route, "0.0.0.0", port)
  }
}

