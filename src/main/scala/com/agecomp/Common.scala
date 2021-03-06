package com

package object agecomp {
  import scala.collection.mutable.HashMap
  type ComponentMap = HashMap[Int, Component]

  def instance[T](className: String, init_args: AnyRef*): T = {
    Class.forName(className).getConstructors.head.newInstance(init_args: _*).asInstanceOf[T]
  }

  object IDGenerator {
    private var _id: Int = -1
    def uid: Int = { _id += 1; _id }
  }
}
