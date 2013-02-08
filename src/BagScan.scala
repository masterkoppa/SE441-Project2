import akka.actor.{ Actor, ActorRef }
import akka.actor.PoisonPill

class BagScan(queueActor: ActorRef, securityActor: ActorRef) extends Actor {

  def receive = {
    case passenger: Passenger => {
      
      //Calculate if the bag will pass or not
      val result = Math.random > .20
      
      //Print out the result
      if (result) {
        printf("Passenger %d's bags are clean.\n", passenger.getId());
        System.out.flush();
      } else {
        printf("Passenger %d's bags set off the alarms.\n", passenger.getId());
        System.out.flush();
      }
      
      //Tell the security actor what's what
      securityActor ! new Result(passenger, result)
    }
    
    //Propagate the dayStart Message
    case dayStart: SystemOnline => {
      securityActor ! dayStart
    }
    
    //Propagate the dayEnd Message
    case dayEnd: SystemOffline => {
      securityActor ! dayEnd
    }
    
  }

  override def postStop() = {
    securityActor ! PoisonPill
  }

}