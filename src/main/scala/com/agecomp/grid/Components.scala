package com.agecomp.grid

import com.agecomp.Component
import javafx.scene.shape.Shape
import javafx.scene.Node
import akka.actor._

class InputComponent extends Component
class MovementInput(var direction: String) extends InputComponent

class Body extends Component {
  var position: Vec2 = (10, 10)
  var heading: Vec2 = (0, 0)
}

class JFXComponent(val node: Shape) extends Component {

}

class Dumbo extends Actor {
  val id = self.path.name.toInt

  val input = new MovementInput("Right")

  def randDirection: String = {
    val r = scala.util.Random
    val i = r.nextInt(4)
    return i match {
      case 0 => "Up"
      case 1 => "Down"
      case 2 => "Left"
      case 3 => "Right"
      case _ => "Down"
    }
  }

  def receive = {
    case _ =>
      input.direction = randDirection
      sender ! input
  }
}
