package com.agecomp

import scala.collection.mutable.HashMap

class Scene() {
  val components = new HashMap[String, ComponentMap]

  def addComponent(id: Int, component: Component) = {
    val className = component.getClass.getName

    if (!components.contains(className)) {
      components.put(className, new ComponentMap())
    }

    val container = components(className)
    component.id = id

    if (!container.contains(id)) {
      container.put(id, component)
    }
  }

  def addEntity(archFactory: () => List[Component]) = {
    val archetype = archFactory()
    val id = IDGenerator.uid

    archetype.foreach(component => {
      addComponent(id, component)
    })
  }

  def container(className: String): ComponentMap = {
    if (components.contains(className)) {
      components(className)
    }
    else {
      new ComponentMap
    }
  }

  def removeComponent(id: Int, container: ComponentMap) = {
    var component = container(id)
    component.destroy
    container.remove(id)
  }

  def removeEntity(id: Int) = {
    for ((className, map) <- components) {
      if (map.contains(id)) {
        removeComponent(id, map)
      }
    }
  }
}

object Scene {
  def apply() = new Scene
}
