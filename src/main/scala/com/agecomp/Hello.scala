package com.agecomp

import scala.collection.mutable.HashMap
import scala.reflect._

import akka.actor._

case object Cara
case object Coroa

class A { var a = 1 }
object A {
  def apply() = new A
}

class B extends A {
  def send(actor: ActorRef) = actor ! Cara
}
object B {
  def apply() = new B
}

class Ambiente extends Environment {
  processors = List[Processor]()

  def receive = {
    case _ =>
      println("Received!")
      manage
      self ! PoisonPill
      context.system.terminate
  }
}

class C(val props: Props) {
}

object Hello extends App {
  val gen = () => Props[Ambiente]

  val actgen = (system: ActorSystem, props: () => Props) => {
    system.actorOf(props(), name="Ambiente")
  }

  val p = Props[Ambiente]
  val c = new C(p)

  val system = ActorSystem("AgeCompSystem")
  val amb = system.actorOf(c.props, name="Limas")

  amb ! "Hi!"
}
