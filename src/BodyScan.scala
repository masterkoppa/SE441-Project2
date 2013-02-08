import akka.actor.{ Actor, ActorRef }

class BodyScan(queueActor: ActorRef, securityActor: ActorRef) extends Actor {

  def receive = {
    case passenger: Passenger => {

      val result = Math.random > .20
      if (result) {
        printf("Passenger %d's body is clean.\n", passenger.getId());
        System.out.flush()
      } else {
        printf("Passenger %d's body sets off the alarms.\n", passenger.getId());
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
  }

}