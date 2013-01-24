import akka.actor.{Actor, ActorRef}
import scala.collection.mutable.HashMap

class Security extends Actor {

  var jail: ActorRef = null
  private var passengerHash = HashMap.empty[Int, Boolean]
  
  def receive = {
    case result: Result => {
      //We've either seen the other result or we have not
      //If we have seen the result before, we can determine the outcome
      val oldRes = passengerHash.get(result.getPassenger().getId())
      if(oldRes.isDefined) {
        passengerHash -= result.getPassenger().getId()
        if(oldRes.get && result.getResult()) {
          //They're FREE!
        } else {
          //Send them to JAIL
          jail ! result.getPassenger()
        }
      } else {
        //Otherwise we store it for later
        passengerHash.put(result.getPassenger().getId(), result.getResult())
      }
    } 
  }
  
}