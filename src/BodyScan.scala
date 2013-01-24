import akka.actor.{Actor, ActorRef}

class BodyScan(queueActor: ActorRef, securityActor: ActorRef) extends Actor {
  
  def receive = {
    case passenger: Passenger => {

      val result = Math.random > .20
      if(result) {
        System.out.println("Passenger %d's body is clean.".format(passenger.getId()));
      } else {
        System.out.println("Passenger %d's body sets off the alarms.".format(passenger.getId()));
      }
      
      //Tell the security actor what's what
      securityActor ! new Result(passenger, result)
      
      //Tell the Queue we're ready for more
      queueActor ! new BodyReady
    }
  }
  
}