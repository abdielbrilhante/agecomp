package com.agecomp.wanderer

import akka.actor.Actor
import akka.actor.Props
import akka.actor.PoisonPill
import akka.actor.ActorSystem
import scala.concurrent.duration._

import javafx.application.Application
import javafx.event.ActionEvent
import javafx.event.EventHandler
import javafx.stage.Stage

object Hello {
  def main(args: Array[String]) {
    Application.launch(classOf[Hello], args: _*)
  }
}

class Hello extends Application {

  val system = ActorSystem("Bakteria")

  override def start(primaryStage: Stage) {
    val environment = system.actorOf(Props(classOf[WandererEnvironment], 10.0, primaryStage))
    environment ! "Tick"
    primaryStage.show
  }

  override def stop {
    system.terminate
  }

}
