package my.demo.kushmakers.actors

import akka.actor.{Actor, ActorLogging, Props}

object KmSupervisor {
  def props(): Props = Props(new KmSupervisor)
}

class KmSupervisor extends Actor with ActorLogging {
  override def preStart(): Unit = log.info("Kush Maker Application started")
  override def postStop(): Unit = log.info("Kush Maker Application stopped")

  // No need to handle any messages
  override def receive: Actor.emptyBehavior.type = Actor.emptyBehavior
}

