import akka.actor.{Actor, ActorRef}

class Driver {
  
  //Initialize some constants
  private val numQueues = 3
  private val passengersPerDay = 30
  
  def main(args: Array[String]) {
    
    //Create the Document Scanner
    var documentScanActor = new DocumentScan()
    var documentScanActorRef = Actor.actorOf(documentScanActor)
    
    //Create the Jail
    var jailActor = new Jail()
    var jailActorRef = Actor.actorOf(jailActor)

    //Build the queues and give them to the document scanner
    documentScanActor.queues = new Array[ActorRef](numQueues)
    for(i <- 0 until numQueues) {
      documentScanActor.queues(i) = buildLine(jailActorRef)
    }
    
    //Start the doc scan and the jail
    documentScanActorRef.start()
    jailActorRef.start()
    
    //Create a number of passengers and send them to the Document checkpoint
    for(i <- 0 until passengersPerDay) {
      documentScanActorRef ! new Passenger(i)
    }
  }
  
  def buildLine(jailActorRef: ActorRef): ActorRef = {

    //Build a Queue
    var queueActor = new QueueActor()
    var queueActorRef = Actor.actorOf(queueActor)
    
    //Build a Bag Scanner
    var bagScanActor = new BagScan()
    var bagScanActorRef = Actor.actorOf(bagScanActor)
    
    //Build a Body Scanner
    var bodyScanActor = new BodyScan()
    var bodyScanActorRef = Actor.actorOf(bodyScanActor)
    
    //Build a Security Checkpoint
    var securityActor = new Security()
    var securityActorRef = Actor.actorOf(securityActor)
    
    //Let all the actors know about the other actors in their line
    queueActor.bagScan = bagScanActorRef
    queueActor.bodyScan = bodyScanActorRef
    bagScanActor.queueActor = queueActorRef
    bagScanActor.securityActor = securityActorRef
    bodyScanActor.queueActor = queueActorRef
    bodyScanActor.securityActor = securityActorRef
    securityActor.jail = jailActorRef
    
    //Start all the actors
    queueActorRef.start()
    bagScanActorRef.start()
    bodyScanActorRef.start()
    securityActorRef.start()
    
    //Return the Queue
    return queueActorRef
  }
  
}