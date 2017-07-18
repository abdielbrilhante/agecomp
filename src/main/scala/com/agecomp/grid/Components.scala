package com.agecomp.grid

import com.agecomp.Component
import com.agecomp.OutputComponent
import com.agecomp.InputComponent
import javafx.scene.shape.Shape
import javafx.scene.Node
import javafx.scene.layout.StackPane
import javafx.application.Platform
import akka.actor._
import scala.collection.mutable.HashMap

class MovementInput(var direction: String) extends InputComponent

class Body extends Component {
  var position: Vec2 = (10, 10)
  var heading: Vec2 = (0, 0)
}

class VisionComponent() extends OutputComponent {
  val output = new HashMap[String, String]

  def addOutput(direction: String, label: String) = {
    output.put(direction, label)
  }

  def fill() = {
    if (!output.contains("Up")) {
      output.put("Up", "Empty")
    }
    if (!output.contains("Down")) {
      output.put("Up", "Empty")
    }
    if (!output.contains("Left")) {
      output.put("Up", "Empty")
    }
    if (!output.contains("Right")) {
      output.put("Up", "Empty")
    }
  }

  def clear() = {
    output.clear()
  }
}

class JFXComponent(val node: Node) extends Component {
  override def destroy = {
    Platform.runLater(() => {
      node.getParent.asInstanceOf[StackPane].getChildren.remove(node)
    })
  }
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
