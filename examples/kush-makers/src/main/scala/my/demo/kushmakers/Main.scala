package my.demo.kushmakers

import akka.actor.ActorSystem
import akka.http.scaladsl.Http
import akka.http.scaladsl.model._
import akka.http.scaladsl.server.Directives._
import akka.stream.ActorMaterializer
import ch.megard.akka.http.cors.scaladsl.CorsDirectives._
import com.typesafe.scalalogging.LazyLogging
import my.demo.kushmakers.actors.{Fleet, KmSupervisor}
import my.demo.kushmakers.entities._
import my.demo.kushmakers.http.HttpSupport
import spray.json._

import scala.concurrent.ExecutionContextExecutor

object Main extends LazyLogging with HttpSupport with JsonSupport {

  def main(args: Array[String]) {

    implicit val system: ActorSystem = ActorSystem("rest-system")
    implicit val materializer: ActorMaterializer = ActorMaterializer()
    implicit val executionContext: ExecutionContextExecutor = system.dispatcher

    val supervisor = system.actorOf(KmSupervisor.props(), "km-supervisor")

    val route =
      pathPrefix(urlpath) {
        logRequest(urlpath) {
          handleErrors {
            cors(corsSettings) {
              path("cmd") {
                get {
                  logger.debug(s"get $urlpath")
                  complete(HttpEntity(ContentTypes.`application/json`,
                                      "{\"msg\": \"Say hello to akka-http\"}\n"))
                } ~
                post {
                  decodeRequest { entity(as[CommandRequest]) { r =>
                    val command = Command(cmd = r.cmd)
                    complete(HttpEntity(ContentTypes.`application/json`, command.toJson.prettyPrint))
                  }}
                }
              } ~
              path("fleet") {
                post {
                  decodeRequest { entity(as[FleetRequest]) { r =>
                    val command = FleetCommand(r.size, r.name)
                    val fleet= system.actorOf( Fleet.props(command), name = command.name)
                    fleet ! "launch"
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

