import akka.actor.{Actor, ActorRef}

class DocumentScan extends Actor {

  var queues: Array[ActorRef] = null
  private var nextQueue = 0
  
  def receive : PartialFunction[Any, Unit] = {
    case passenger: Passenger => {
      //TODO: Add random check here to turn away passenger
      
      //Pass the passenger on to the proper queue
      queues(nextQueue) ! passenger
      
      //Make sure we send the next passenger to the next queue
      nextQueue = (nextQueue + 1) % queues.length
    }
  }
  
}