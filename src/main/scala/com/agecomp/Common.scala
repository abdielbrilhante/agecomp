package com

package object agecomp {
  import scala.collection.mutable.HashMap
  type ComponentMap = HashMap[Int, Component]

  // TODO: add tuple arg for construtor parameters
  def instance[T](className: String): T = {
    Class.forName(className).newInstance().asInstanceOf[T]
  }
}
