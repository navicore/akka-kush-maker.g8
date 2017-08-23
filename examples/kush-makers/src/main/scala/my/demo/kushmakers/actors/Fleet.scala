package my.demo.kushmakers.actors

import akka.actor.{Actor, Props}
import akka.event.LoggingReceive
import my.demo.kushmakers.entities.FleetCommand

object Fleet {
  def props(command: FleetCommand): Props = Props(new Fleet(command))
}

class Fleet(command: FleetCommand) extends Actor {

  override def receive: PartialFunction[Any, Unit] = LoggingReceive {
    case "launch" => println("launching!")
    case _       => println("huh?")
  }

}

