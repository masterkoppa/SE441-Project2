import akka.actor.{ Actor, ActorRef, Actors }
import scala.collection.mutable.HashMap
import akka.actor.PoisonPill

class Security() extends Actor {
  private var passengerHash = HashMap.empty[Int, Boolean]
  var jail: ActorRef = null
  var shutdownExpected: Boolean = false
  
  def this(jailActor: ActorRef) = {
    this()
    jail = jailActor
  }

  def receive = {
    case dayStart: SystemOnline => {
      shutdownExpected = false
      jail ! dayStart
    }

    case dayEnd: SystemOffline => {
      if (shutdownExpected && passengerHash.size == 0) {
        jail ! dayEnd
      }
      shutdownExpected = true
    }

    case result: Result => {
      //We've either seen the other result or we have not
      //If we have seen the result before, we can determine the outcome
      val oldRes = passengerHash.get(result.getPassenger().getId())
      if (oldRes.isDefined) {
        if (oldRes.get && result.getResult()) {
          //They're FREE!
          printf("Passenger %d is sent on their way.\n", result.getPassenger().getId())
          System.out.flush()
        } else {
          //Send them to JAIL
          jail ! result.getPassenger()
        }
        passengerHash -= result.getPassenger().getId()

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
    Actors.registry().shutdownAll()
  }
}