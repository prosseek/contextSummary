package simulation.grapevineType

import grapevineType.{StringType, BottomType, TemperatureType}
import simulation.theoryFalsePositives._

//import simulation.theoryFalsePositives.Temperature

/**
 * Created by smcho on 9/5/14.
 */
class TemperatureSimulation (config:Map[String, Int]) extends Simulation(config) {
  def getTermperature(ba:Array[Byte]) = {
    val bits = new TemperatureType
    if (bits.fromByteArray(ba) == BottomType.NoError) {
      //println(bits.value)
      Some(bits)
    }
    else None
  }

  def getRandomTemperature(byteWidth:Int = 1) = {
    getTermperature(Simulation.getRandomByteArray(byteWidth))
  }

  /*
    GET THEORETICAL FP TIME/DATE
  */
  def getTheory(bytes:Int = TemperatureType.getSize) :Map[String, Double] = {
    Map[String, Double](
      "theory_fp" -> Temperature.fp(bytes),
      "theory_fp_pair" -> Temperature.fp_pair(bytes)
    )
  }
  /*
    GENERATE TABLES - RUN TESTS
  */
  def run(width:Int, size:Int, near:Int, lat:Boolean) = {
    var bytes = math.max(width, TemperatureType.getSize)
    var bottom_computation = 0
    var fp = 0
    var bottom_relation_pair = 0
    var fp_pair = 0
    var bottom_relation_near = 0
    var fp_near = 0

    (1 to size).foreach { i =>
      val la = getRandomTemperature(bytes)
      if (la.isEmpty)
        bottom_computation += 1 // get bottom_computation
      else {
        fp += 1
        val s = new StringSimulation(config)
        val ra = s.getRandomString(StringType.getMiniumLength)
        if (ra.isDefined) {
          fp_pair += 1
        }
      }
    }

    Map[String, Double](
      "fp" -> fp/size.toDouble,
      "fp_pair" -> fp_pair/size.toDouble
    ) ++ getTheory(bytes)
  }

  override def simulate(width:Int) :Map[String,Double] = {
    val iteration = config("iteration")
    // -1 means I'm not using it
    runAndAverage(iteration = iteration, f = () => run(width = width, size = config("size"), near = -1, lat = true))
  }
}

// Just test code to print out
object TemperatureSimulation extends App {
  var m = Map[String,Int]("size" -> 100000, "iteration" -> 10, "verbose" -> 0)
  var ls = new TemperatureSimulation(m)
  val f = (i :Int) => ls.simulate(width = i)

  (1 to 1).foreach { i =>
    println(s"SIMULATION FOR Age Temperature ${i}")
    println(s"${Util.map2string(f(i))}")
  }

  //  m = Map[String,Int]("size" -> 100, "near" -> 10, "iteration" -> 10, "verbose" ->0)

  //  val res = ls.simulateOverWidth(1,10)
  //  println(res.mkString("\n\n"))
}