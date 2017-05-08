package com.agecomp

import akka.actor.Actor
import akka.actor.Props

abstract class Environment extends Actor {
  var processors: List[Component] = Nil
  val scene = Scene()

  def create(id: Int) = {
    // create Akka.Actor, set flag to Running
  }

  def remove(id: Int) = {
    // remove Actor
  }

  def manage = {
    val agents = scene.container("com.agecomp.AgentRef")

    for ((id, component) <- agents) {
      val agent = component.asInstanceOf[AgentRef]
      agent.flag match {
        case "Create" => create(id)
        case "Remove" => remove(id)
        case _ => Unit
      }
    }
  }
}
