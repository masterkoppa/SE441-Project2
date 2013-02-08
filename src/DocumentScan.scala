import akka.actor.{ Actor, ActorRef }
import akka.actor.PoisonPill

class DocumentScan(queues: Array[ActorRef]) extends Actor {

  private var nextQueue = 0
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

      System.out.println("Passenger %d arrives at the document scanner.".format(passenger.getId()))
      if (Math.random > 0.20) {
        System.out.println("Passenger %d has valid documents.".format(passenger.getId()))

        //Pass the passenger on to the proper queue
        queues(nextQueue) ! passenger

        //Make sure we send the next passenger to the next queue
        nextQueue = (nextQueue + 1) % queues.length
      } else {
        System.out.println("Passenger %d is turned away.".format(passenger.getId()))
      }
    }

    case dayStart: SystemOnline => {
      
      queues foreach (q => q ! dayStart)
    }

    case dayEnd: SystemOffline => {
      queues foreach (q => q ! dayEnd)
    }
  }

  override def postStop() = {
    printf("Document Scan killed by PoisonPill, killing everyone else\n")
    for (i <- 0 until count) {
      queues(i) ! PoisonPill
    }
  }
}