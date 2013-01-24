import akka.actor.{Actor, ActorRef}

class BagScan extends Actor {

  var queueActor: ActorRef = null
  var securityActor: ActorRef = null
  
  def receive = {
    case passenger: Passenger => {
      
      val result = Math.random > .20
      if(result) {
        System.out.println("Passenger %d's bags are clean.".format(passenger.getId()));
      } else {
        System.out.println("Passenger %d's bags set off the alarms.".format(passenger.getId()));
      }
      
      //Tell the security actor what's what
      securityActor ! new Result(passenger, result)
      
      //Tell the Queue we're ready for more
      queueActor ! new BagReady
    }
  }
  
}