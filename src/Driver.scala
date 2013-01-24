import akka.actor.{Actor, ActorRef}

object Driver {
  
  //Initialize some constants
  private val numQueues = 3
  private val passengersPerDay = 30
  
  def main(args: Array[String]) {
    
    val jail = Actor.actorOf[Jail]
    jail.start()
    
    val documentScan = Actor.actorOf(new DocumentScan(numQueues, jail))
    documentScan.start()
    
    //Add some Passengers
    for(i <- 0 until passengersPerDay) {
      documentScan ! new Passenger(i)
    }
  }
  
}