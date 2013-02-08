import akka.actor.{ Actor, ActorRef }
import scala.collection.mutable.HashMap
import akka.actor.Actors

class Security(jail: ActorRef) extends Actor {
  private var passengerHash = HashMap.empty[Int, Boolean]

  var shutdownExpected: Boolean = false

  def receive = {
    case dayStart: SystemOnline => {
      shutdownExpected = false
      jail ! dayStart
    }

    case dayEnd: SystemOffline => {
      shutdownExpected = true
      if (passengerHash.size == 0) {
        jail ! dayEnd
      }
    }

    case result: Result => {
      //We've either seen the other result or we have not
      //If we have seen the result before, we can determine the outcome
      val oldRes = passengerHash.get(result.getPassenger().getId())
      if (oldRes.isDefined) {
        passengerHash -= result.getPassenger().getId()
        if (oldRes.get && result.getResult()) {
          //They're FREE!
          printf("Passenger %d is sent on their way.\n", result.getPassenger().getId())
        } else {
          //Send them to JAIL
          printf("Passenger %d is sent straight to jail and will not pass Go or collect $200.\n", result.getPassenger().getId())
          jail ! result.getPassenger()
        }

        if (shutdownExpected && passengerHash.size == 0) {
          jail ! new SystemOffline()
        }
      } else {
        //Otherwise we store it for later
        passengerHash.put(result.getPassenger().getId(), result.getResult())
      }
    }
  }

  override def postStop() = {
    printf("Security killed by PoisonPill, killing everyone else\n")
    Actors.registry().shutdownAll()
  }
}