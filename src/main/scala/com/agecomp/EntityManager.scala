package com.agecomp

import scala.collection.mutable.HashMap

class EntityManager() {
  val components = new HashMap[String, ComponentMap]
  private var _id: Int = 0

  // TODO: Better uid generation
  def uid: Int = { _id += 1; _id }

  // TODO: Check if component name is valid
  def addEntity(blueprint: List[(String, AnyRef)]) = {
    val id = uid  

    blueprint.foreach(component => {
      val componentName = component._1
      val params = component._2

      if (!components.contains(componentName)) {
        components.put(componentName, new ComponentMap())
      }

      val container = components(componentName)

      val c = instance[Component](componentName, Int.box(id), params)
      container.put(id, c)
    })

    println(components)
  }
}
