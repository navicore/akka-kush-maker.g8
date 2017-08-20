package my.demo.kushmakers.entities

import java.util.{Date, UUID}

final case class Command(cmd: String, timestamp: Date = new Date(), id: UUID = UUID.randomUUID())
final case class FleetCommand(size: Int, timestamp: Date = new Date(), id: UUID = UUID.randomUUID(), cmd: String = "create fleet")

final case class CommandRequest(cmd: String) {
  require(!cmd.isEmpty && cmd.startsWith("do"), "cmd must start with 'do'")
}
final case class FleetRequest(size: Int) {
require(size >= 0 && size <= 100000, "can not request a fleet larger than 100,000 ships")
require(size > 0, "a fleet must have at least one ship")
}

