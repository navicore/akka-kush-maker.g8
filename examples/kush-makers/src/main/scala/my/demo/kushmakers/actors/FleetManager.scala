package my.demo.kushmakers.actors

import akka.actor.{Actor, ActorLogging, Props}
import akka.event.LoggingReceive
import my.demo.kushmakers.entities.{FleetCommand, FleetRequest}

object FleetManager {
  def props(): Props = Props(new FleetManager)
}

class FleetManager extends Actor with ActorLogging {
  override def preStart(): Unit = log.info("Kush Maker Application started")
  override def postStop(): Unit = log.info("Kush Maker Application stopped")

  override def receive: PartialFunction[Any, Unit] = LoggingReceive {
    case r: FleetRequest =>
      log.info("launching!")
      val c = FleetCommand(r.size, r.name)
      val fleet = context.actorOf( Fleet.props(c), name = c.name)
      fleet ! "build"
      fleet ! "launch"
      sender() ! c
    case _       => println("huh?")
  }
}

