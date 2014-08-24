package grapevineType

import grapevineType.BottomType._
import util.conversion.ByteArrayTool

/**
 * Created by smcho on 8/11/14.
 */

object StringType {
  def isPrintable(v:Char) = {
    (v >= 0x20 && v <= 0x7E)
  }
  def getId = 4
}

case class StringType(input:String) extends GrapevineType {
  if (input != null) set(input)
  def this() = this(null)

  def check(value:String) = {
    assert(value.size < 256) // 1 byte can describe 0 to 255, so 255 is the maximum
    value.forall {StringType.isPrintable}
  }
  def set(value: Any) : Unit = {
    if (check(value.asInstanceOf[String])) this.value = value
    else throw new RuntimeException(s"String Error: [${value}]")
  }

  override def get() : String = this.value.asInstanceOf[String]

  override def toByteArray(goalSize: Int): Array[Byte] = {
    val v = this.value.asInstanceOf[String]
    val size = if (goalSize == -1) (v.size + 1) else goalSize
    ByteArrayTool.stringToByteArray(v, size)
  }

  def fromByteArray(ba: Array[Byte]): BottomType = {
    try {
      val size = (ByteArrayTool.byteToUnsigned(ba(0)) + 1) // pascal type string
      if (super.fromByteArray(ba, byteSize = size, f = ByteArrayTool.byteArrayToString) == NoError) {
        val result = this.value.asInstanceOf[String]
        if (check(result)) {
          NoError
        } else {
          Computational
        }
      }
      else {
        Computational
      }
    }
    // whenever we have an error, we return false
    catch {
      case e: Exception => Computational
    }

  }
  override def getId = StringType.getId
  override def getSize = this.value.asInstanceOf[String].size + 1 // for 1 byte to indicate the size
  override def getTypeName() = "StringType"
}