import akka.actor.{ Actor, ActorRef, PoisonPill }

class Driver extends Actor {

  //Initialize some constants
  private val numQueues = 3
  private val passengersPerDay = 2
  private val numDays = 2

  var currentDay = 0
  val jail = Actor.actorOf(new Jail(numQueues, self)).start()
  val documentScan = Actor.actorOf(new DocumentScan(numQueues, jail)).start()

  def nextDay() {
    if (currentDay < numDays) {
      currentDay += 1
      print("A new day has begun!\n")
      documentScan ! new SystemOnline()
    }
  }

  def receive = {
    case begin: BeginSimulation => {
      nextDay()
    }

    case online: SystemOnline => {
      //Send Passengers into the system
      for (i <- 0 until passengersPerDay) {
        documentScan ! new Passenger(i)
      }

      //Take the system offline
      documentScan ! new SystemOffline()
    }

    case offline: SystemOffline => {
      print("The day is over!\n")
      nextDay()
    }
  }

}