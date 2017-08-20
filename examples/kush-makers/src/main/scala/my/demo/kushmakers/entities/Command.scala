package my.demo.kushmakers.entities

import java.util.{Date, UUID}

final case class Command(cmd: String, timestamp: Date = new Date(), id: UUID = UUID.randomUUID())

