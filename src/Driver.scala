import akka.actor.{ Actor, ActorRef, PoisonPill }

class Driver extends Actor {

  //Initialize some constants
  private val numQueues = 3
  private val passengersPerDay = 2
  private val numDays = 2

  var currentDay = 0
  val jail = Actor.actorOf(new Jail(numQueues, self)).start()
  val documentScan = Actor.actorOf(new DocumentScan(numQueues, jail)).start()

  //Try to restart the system for a second time as a proof of concept
  def nextDay() {
    if (currentDay < numDays) {
      currentDay += 1
      printf("A new day has begun!")
      System.out.flush()
      documentScan ! new SystemOnline()
    } else {
      documentScan ! PoisonPill
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
      printf("The day is over!")
      System.out.flush()
      nextDay()
    }
  }

}