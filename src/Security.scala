import akka.actor.{Actor, ActorRef}
import scala.collection.mutable.HashMap

class Security(jail: ActorRef) extends Actor {
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
          System.out.println("Passenger %d is sent on their way.".format(result.getPassenger().getId()))
        } else {
          //Send them to JAIL
          System.out.println("Passenger %d is sent straight to jail and will not pass Go or collect $200.".format(result.getPassenger().getId()))
          jail ! result.getPassenger()
        }
      } else {
        //Otherwise we store it for later
        passengerHash.put(result.getPassenger().getId(), result.getResult())
      }
    } 
  }
  
}