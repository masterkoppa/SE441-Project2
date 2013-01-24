import akka.actor.{Actor, ActorRef}

class DocumentScan(queueCount: Int) extends Actor {

  var queues: Array[ActorRef] = new Array[ActorRef](queueCount)
  private var nextQueue = 0
  
  def receive : PartialFunction[Any, Unit] = {
    case passenger: Passenger => {
      
      System.out.println("Passenger %d arrives at the document scanner.".format(passenger.getId()))
      if(Math.random > 0.20) {
        System.out.println("Passenger %d has valid documents.".format(passenger.getId()))
        
        //Pass the passenger on to the proper queue
        queues(nextQueue) ! passenger
	      
        //Make sure we send the next passenger to the next queue
        nextQueue = (nextQueue + 1) % queues.length
      } else {
        System.out.println("Passenger %d is turned away.".format(passenger.getId()))
      }
    }
  }
  
}