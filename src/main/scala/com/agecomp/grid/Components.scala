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
      output.put("Down", "Empty")
    }
    if (!output.contains("Left")) {
      output.put("Left", "Empty")
    }
    if (!output.contains("Right")) {
      output.put("Right", "Empty")
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
