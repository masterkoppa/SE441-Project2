import akka.actor.{ Actor, ActorRef }
import scala.collection.mutable.Queue
import akka.actor.PoisonPill

class QueueActor(jail: ActorRef) extends Actor {

  var security = Actor.actorOf(new Security(jail)).start()
  var bagScan: ActorRef = Actor.actorOf(new BagScan(this.self, security)).start()
  var bodyScan: ActorRef = Actor.actorOf(new BodyScan(this.self, security)).start()

  private var shutdownRequested: Boolean = false

  def receive = {
    case passenger: Passenger => {
      bagScan ! passenger
      bodyScan ! passenger
    }
  
    //Propagate the dayStart Message
    case dayStart: SystemOnline => {
      bagScan ! dayStart
      bodyScan ! dayStart
    }
    
    //Propagate the dayEnd Message
    case dayEnd: SystemOffline => {
      bagScan ! dayEnd
      bodyScan ! dayEnd
    }
  }

  override def postStop() = {
    bagScan ! PoisonPill
    bodyScan ! PoisonPill
  }

}