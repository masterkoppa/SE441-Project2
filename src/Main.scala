import akka.actor.{ Actor }

object Main {

  def main(args: Array[String]) {
	  Actor.actorOf[Driver].start() ! new BeginSimulation()
  }

}