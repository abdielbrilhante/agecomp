package com.agecomp

import scala.collection.mutable.HashMap

class EntityManager(val components: HashMap[String, ComponentMap]) {
  private var _id: Int = 0

  // TODO: Better uid generation
  def uid: Int = { _id += 1; _id }

  // TODO: Check if component name is valid
  def construct(blueprint: List[(String, Product)]) = {
    blueprint.foreach(component => {
      val componentName = component._1

      if (!components.contains(componentName)) {
        components.put(componentName, new ComponentMap())
      }

      val container = components(componentName)
      container.put(uid, instance(componentName))
    })
  }
}
