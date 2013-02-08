import akka.actor.{ Actor, ActorRef }
import scala.collection.mutable.Set

class Jail(numQueues: Int, driver: ActorRef) extends Actor {

  var detainees = Set.empty[Int]
  var readyQueues = 0
  var emptyQueues = 0

  def receive = {
    case passenger: Passenger => {
      printf("Passenger %d is being prepared for gitmo.\n", passenger.getId())
      detainees += passenger.getId()
    }

    case dayStart: SystemOnline => {
      readyQueues += 1
      emptyQueues = 0

      if (readyQueues == numQueues) {
        driver ! dayStart
      }
    }

    case dayEnd: SystemOffline => {
      emptyQueues += 1
      readyQueues = 0

      if (emptyQueues == numQueues) {
        detainees foreach (id => printf("Passenger %d has been teleported to gitmo.\n", id))
        detainees.clear()
        driver ! dayEnd
      }
    }

  }

}