package com.agecomp.grid

import com.agecomp.Component
import com.agecomp.EntityLabel
import com.agecomp.Processor
import com.agecomp.Scene
import com.agecomp.AgentRef

import javafx.stage.Stage
import javafx.application.Platform
import javafx.scene.layout.TilePane
import javafx.scene.layout.VBox
import javafx.scene.paint.Color
import javafx.scene.shape.Rectangle
import javafx.scene.shape.Circle
import javafx.scene.Node
import javafx.scene.layout.Pane
import javafx.scene.layout.StackPane

import akka.actor.Props

class Grid(val rows: Int, val cols: Int) {
  def count: Int = rows*cols
}

class MovementProcessor(sc: Scene) extends Processor(sc) {
  override def run() {
    val agents = scene.container("com.agecomp.grid.MovementInput")
    val bodies = scene.container("com.agecomp.grid.Body")

    for ((id, component) <- agents) {
      val input = component.asInstanceOf[MovementInput]
      val heading = input.direction match {
        case "Up" => (0, -1)
        case "Down" => (0, 1)
        case "Left" => (-1, 0)
        case "Right" => (1, 0)
        case _ => (0, 0)
      }
      val body = bodies(id).asInstanceOf[Body]
      body.heading = heading
    }
  }
}

class VisionProcessor(sc: Scene, val grid: Grid) extends Processor(sc) {
  override def run() {
    val visionComponents = scene.container("com.agecomp.grid.VisionComponent")
    val outputs = scene.container("com.agecomp.OutputComponent")
    val bodies = scene.container("com.agecomp.grid.Body")
    val labels = scene.container("com.agecomp.EntityLabel")

    for ((id, component) <- visionComponents) {
      val output = component.asInstanceOf[VisionComponent]
      val (ax, ay) = bodies(id).asInstanceOf[Body].position

      output.clear()
      for ((bodyId, bodyComp) <- bodies) {
        val (bx, by) = bodies(bodyId).asInstanceOf[Body].position
        val label = labels(bodyId).asInstanceOf[EntityLabel].label

        if ((ax == bx) && (ay - by) == 1) {
          output.addOutput("Up", label)
        }
        if ((ax == bx) && (by - ay) == 1) {
          output.addOutput("Down", label)
        }
        if ((ay == by) && (ax - bx) == 1) {
          output.addOutput("Left", label)
        }
        if ((ay == by) && (bx - ax) == 1) {
          output.addOutput("Right", label)
        }
      }

      output.fill()
      // println(output.output)
      outputs(id) = output
    }
  }
}

class PhysicsProcessor(sc: Scene, val grid: Grid) extends Processor(sc) {
  override def run() {
    val bodies = scene.container("com.agecomp.grid.Body")

    for ((id, component) <- bodies) {
      val body = component.asInstanceOf[Body]
      val (px, py) = body.position
      val (hx, hy) = body.heading

      val dx = px + hx
      val dy = py + hy

      if (dx > -1 && dx < grid.cols && dy > -1 && dy < grid.rows) {
        body.position = (dx, dy)
        // if there is another body in that position, COLLISION
      }

      body.heading = (0, 0)
    }
  }
}

class JFXProcessor(sc: Scene, val grid: Grid, val stage: Stage) extends Processor(sc) {
  val root = new TilePane
  root.setHgap(0)
  root.setVgap(0)
  root.setPrefColumns(grid.cols)
  root.setPrefRows(grid.rows)

  Platform.runLater(() => {
    for (index <- 1 to grid.count) {
      val pane = new StackPane
      val rect = new Rectangle(24, 24)

      if (index % 2 == 0) {
        rect.setFill(Color.color(1, 1, 1))
      }
      else {
        rect.setFill(Color.color(0.9, 0.925, 0.95))
      }

      root.getChildren.add(pane)
      pane.getChildren.add(rect)
    }
    stage.getScene.getRoot.asInstanceOf[VBox].getChildren.add(root)
    stage.sizeToScene()
  })

  // TilePane -> Pane -> Rectangle -> (food -> bacteria)

  def translate(node: Node, position: Vec2) {
    val (x, y) = position
    val index = x + y*grid.cols

    Platform.runLater(() => {
      val pane = root.getChildren.get(index).asInstanceOf[Pane]
      if (node.getParent != pane) {
        pane.getChildren.add(node)
      }
    })
  }

  override def run() {
    val bodies = scene.container("com.agecomp.grid.Body")
    val jfxc = scene.container("com.agecomp.grid.JFXComponent")

    for ((id, component) <- jfxc) {
      val body = bodies(id).asInstanceOf[Body]
      val jfx = component.asInstanceOf[JFXComponent]
      val position = body.position

      translate(jfx.node, position)
    }
  }
}
