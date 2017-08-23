package my.demo.kushmakers.actors

import akka.actor.{Actor, ActorLogging, Props}
import akka.event.LoggingReceive
import my.demo.kushmakers.entities.FleetCommand

object Fleet {
  def props(command: FleetCommand): Props = Props(new Fleet(command))
}

class Fleet(command: FleetCommand) extends Actor with ActorLogging {

  override def receive: PartialFunction[Any, Unit] = LoggingReceive {
    case "build" => log.info("building...")
    case "launch" => log.info("launched!")
    case _       => println("huh?")
  }

}

