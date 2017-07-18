package com.agecomp.dts
import com.agecomp.Environment
import com.agecomp.AgentRef
import com.agecomp.InputComponent

import scala.concurrent.duration._
import scala.language.postfixOps
import akka.actor.PoisonPill
import javafx.stage.Stage

class FixedTimeEnvironment(val fps: Double, stage: Stage) extends Environment {
  import context._

  def sendPerceptions() = {}

  def timestep() {
    context.system.scheduler.scheduleOnce(1000/fps millis, self, "Tick")
    manage
    processors.foreach(p => p.run)
    val agents = scene.container("com.agecomp.AgentRef")

    sendPerceptions()
  }

  def cache(m: InputComponent, name: String) {
    scene.addComponent(name.toInt, m)
  }

  def receive = {
    case "Tick" => timestep()
    case m: InputComponent => cache(m, sender.path.name)
    case _ => println("Unrecognized message")
  }
}
