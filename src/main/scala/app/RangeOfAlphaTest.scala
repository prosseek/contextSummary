package app

import core.BloomierFilterSummary

import scala.collection.mutable.{Map => MMap}

/**
 * Created by smcho on 9/11/14.
 */
object RangeOfAlphaTest extends App {
  var nmax = Int.MinValue
  var nmin = Int.MaxValue
  var mmax = Int.MinValue
  var mmin = Int.MaxValue
  var alphasum = 0.0
  val count = 1000000

  def calculate2(i:Int, bf: BloomierFilterSummary) : Unit = {
    val n = bf.getN()
    val m = bf.getM()
    //println(s"${n}-${m} => ${m.toDouble/n}")
    if (n > nmax) nmax = n
    if (n < nmin) nmin = n
    if (m > mmax) mmax = m
    if (m < mmin) mmin = m
    alphasum += (m.toDouble)/n
  }

  val conf = MMap[String, Any]()
  conf("iteration") = count
  conf("byteWidth") = -1 // random byte width
  GenerateContexts.parallelExecute(configuration = conf.toMap, calculate2)
  println(s"N(${nmin} - ${nmax}) M(${mmin} - ${nmax}) ALPHA AVG ${alphasum/count})")
}
