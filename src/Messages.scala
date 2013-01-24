
class Passenger(id: Int) {
  def getId() = id
} 

class BagReady()

class BodyReady()

class Result(passenger : Passenger, result : Boolean) {
  def getPassenger() = passenger
  def getResult() = result
}
