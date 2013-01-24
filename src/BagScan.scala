import akka.actor.{Actor, ActorRef}

class BagScan extends Actor {

  var queueActor: ActorRef = null
  var securityActor: ActorRef = null
  
  def receive = {
    case passenger: Passenger => {
      //TODO: Add random chance to this
      
      //Tell the security actor what's what
      securityActor ! new Result(passenger, true)
      
      //Tell the Queue we're ready for more
      queueActor ! new BagReady
    }
  }
  
}