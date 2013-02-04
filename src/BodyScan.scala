import akka.actor.{Actor, ActorRef}

class BodyScan(queueActor: ActorRef, securityActor: ActorRef) extends Actor {
  
  def receive = {
    case passenger: Passenger => {

      val result = Math.random > .20
      if(result) {
        printf("Passenger %d's body is clean.\n", passenger.getId());
      } else {
        printf("Passenger %d's body sets off the alarms.\n", passenger.getId());
      }
      
      //Tell the security actor what's what
      securityActor ! new Result(passenger, result)
      
      //Tell the Queue we're ready for more
      queueActor ! new BodyReady
    }
  }
  
  override def postStop() = {
	  printf("BodyScan killed by PoisonPill, killing everyone else\n")
  }
  
}