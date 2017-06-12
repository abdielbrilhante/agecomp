package com.agecomp.grid

import com.agecomp.Component
import com.agecomp.Processor
import com.agecomp.Scene
import com.agecomp.AgentRef

import javafx.stage.Stage
import javafx.application.Platform
import javafx.scene.layout.TilePane
import javafx.scene.paint.Color
import javafx.scene.shape.Rectangle
import javafx.scene.shape.Circle
import javafx.scene.shape.Shape
import javafx.scene.layout.Pane
import javafx.scene.layout.StackPane

import akka.actor.Props

class Grid(val rows: Int, val cols: Int) {
  def count: Int = rows*cols
}

class Factory(sc: Scene) extends Processor(sc) {
  val bacteria = () => List[Component](
    new Body(), new JFXComponent(new Circle(8, Color.RED)), new AgentRef(Props[Dumbo])
  )
  val bacteria2 = () => List[Component](
    new Body(), new JFXComponent(new Circle(8, Color.INDIGO)), new AgentRef(Props[Dumbo])
  )

  var count = 0

  override def run() {
    if (count < 1) {
      scene.addEntity(bacteria)
      scene.addEntity(bacteria2)
    }
    count += 1
  }
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
      }

      body.heading = (0, 0)
    }
  }
}

class JFXProcessor(sc: Scene, val grid: Grid, val stage: Stage) extends Processor(sc) {
  val root = new TilePane
  root.setHgap(1)
  root.setVgap(1)

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
    stage.setTitle("AgeComp JFX Environment")
    stage.setScene(new javafx.scene.Scene(root, grid.cols*25 - 1, grid.rows*25 - 1))
  })

  // TilePane -> Pane -> Rectangle -> (food -> bacteria)

  def translate(shape: Shape, position: Vec2) {
    val (x, y) = position
    val index = x + y*grid.cols

    Platform.runLater(() => {
      val pane = root.getChildren.get(index).asInstanceOf[Pane]
      if (shape.getParent != pane) {
        pane.getChildren.add(shape)
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
