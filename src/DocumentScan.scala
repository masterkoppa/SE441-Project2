import akka.actor.{ Actor, ActorRef }
import akka.actor.PoisonPill

class DocumentScan(queues: Array[ActorRef]) extends Actor {

  private var nextQueue = 0;
  private var count = 0;
  
  def this(queueCount: Int, jail: ActorRef) = {
    this(new Array[ActorRef](queueCount))
    count = queueCount
    for (i <- 0 until queueCount) {
      queues(i) = Actor.actorOf(new QueueActor(jail))
      queues(i).start()
    }
  }

  def receive: PartialFunction[Any, Unit] = {
    case passenger: Passenger => {
      
      printf("Passenger %d arrives at the document scanner.\n", passenger.getId())
      System.out.flush()
      
      //Determine if the passenger will be turned away or not
      if (Math.random > 0.20) {
        printf("Passenger %d has valid documents.\n", passenger.getId())
        System.out.flush()
        
        //Pass the passenger on to the proper queue
        queues(nextQueue) ! passenger

        //Make sure we send the next passenger to the next queue
        nextQueue = (nextQueue + 1) % queues.length
      } else {
        printf("Passenger %d is turned away.\n", passenger.getId())
        System.out.flush()
      }
    }
    
    //Propagate the dayStart Message
    case dayStart: SystemOnline => {
      queues foreach (q => q ! dayStart)
    }

    //Propagate the dayEnd Message
    case dayEnd: SystemOffline => {
      queues foreach (q => q ! dayEnd)
    }
  }

  //Propagate the PosionPill down the chain
  override def postStop() = {
    for (i <- 0 until count) {
      queues(i) ! PoisonPill
    }
  }
}