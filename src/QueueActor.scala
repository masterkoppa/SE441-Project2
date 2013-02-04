import akka.actor.{Actor, ActorRef}
import scala.collection.mutable.Queue
import akka.actor.PoisonPill

class QueueActor extends Actor {
 
  var bagScan: ActorRef = null
  var bodyScan: ActorRef = null
  
  def this(jail: ActorRef) {
    this()
    
    var security = Actor.actorOf( new Security(jail) )
    security.start()
    
    bagScan = Actor.actorOf(new BagScan(this.self, security))
    bagScan.start()
    
    bodyScan = Actor.actorOf(new BodyScan(this.self, security))
    bodyScan.start()
    
  }
  
  private var bagStatus: Boolean = true
  private var bodyStatus: Boolean = true
  
  private val bagQueue = Queue.empty[Passenger]
  private val bodyQueue = Queue.empty[Passenger]
  
  def tryHandle() = {
    if(bagStatus && bagQueue.length > 0) {
      bagScan ! bagQueue.dequeue()
      bagStatus = false
    }
    
    if(bodyStatus && bodyQueue.length > 0) {
      bodyScan ! bodyQueue.dequeue()
      bodyStatus = false
    }
  }
  
  def receive = {
    case passenger : Passenger => {
      bagQueue.enqueue(passenger)
      bodyQueue.enqueue(passenger)
      tryHandle()
    }
    
    case bagReady : BagReady => {
      bagStatus = true
      tryHandle()
    }
    
    case bodyReady : BodyReady => {
      bodyStatus = true
      tryHandle()
    }
  }
  
  override def postStop() = {
	  printf("Queue killed by PoisonPill, killing everyone else\n")
	  bagScan ! PoisonPill
	  bodyScan ! PoisonPill
  }
  
}