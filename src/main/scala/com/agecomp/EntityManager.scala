package com.agecomp

import scala.collection.mutable.HashMap

class EntityManager() {
  val components = new HashMap[String, ComponentMap]
  private var _id: Int = 0

  // TODO: Better uid generation
  def uid: Int = { _id += 1; _id }

  // TODO: Check if component already exists
  def addComponent(id: Int, name: String, params: AnyRef) = {
    if (!components.contains(name)) {
      components.put(name, new ComponentMap())
    }

    val component = instance[Component](name, Int.box(id), params)
    components(name).put(id, component)
  }

  def addEntity(blueprint: List[(String, AnyRef)]) = {
    blueprint.foreach(component => {
      addComponent(uid, component._1, component._2)
    })
    println(components)
  }
}
