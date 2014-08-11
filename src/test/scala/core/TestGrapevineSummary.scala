package core

import dataType.GrapevineType
import org.scalatest._

// Grapevine is an abstract class, so we need to make a concrete class just to test GrapevineSummary
class A extends GrapevineSummary {
  override def getSize(): Int = 0
  override def get(key: String): Option[AnyVal] = None
}

/**
 * Created by smcho on 8/10/14.
 */

class TestGrapevineSummary extends FunSuite  {
  test ("Test create") {
    val m = Map[String, AnyVal]("A"->10, "B"->20.5)
    val a = new A()
    a.create(m)
    assert(a.getTypeValue("A").get == Tuple2(GrapevineType.Integer, 10))
    assert(a.getTypeValue("B").get == Tuple2(GrapevineType.FloatingPoint, 20.5))
    assert(a.getTypeValue("C").isEmpty)
  }
}
