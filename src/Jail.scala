import akka.actor.{Actor, ActorRef}

class Jail extends Actor {

  def receive = {
    case passenger: Passenger => {
      
      printf("Passenger %d is being prepared for incineration, Bye!\n", passenger.getId())
    }
  }
  
}