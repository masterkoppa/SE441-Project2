import akka.actor.{ Actor, ActorRef }
import akka.actor.PoisonPill

class BagScan(queueActor: ActorRef, securityActor: ActorRef) extends Actor {

  def receive = {
    case passenger: Passenger => {

      val result = Math.random > .20
      if (result) {
        System.out.println("Passenger %d's bags are clean.".format(passenger.getId()));
        System.out.flush()
      } else {
        System.out.println("Passenger %d's bags set off the alarms.".format(passenger.getId()));
        System.out.flush()
      }

      //Tell the security actor what's what
      securityActor ! new Result(passenger, result)
    }

    case dayStart: SystemOnline => {
      securityActor ! dayStart
    }

    case dayEnd: SystemOffline => {
      securityActor ! dayEnd
    }
  }

  override def postStop() = {
    securityActor ! PoisonPill
  }

}