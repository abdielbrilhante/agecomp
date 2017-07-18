package com.agecomp

abstract class Component {
  private var _id = -1

  def id = _id

  def id_= (value: Int):Unit = { _id = value }

  def destroy = {}
}

class InputComponent extends Component

class OutputComponent extends Component

class EntityLabel(val label: String, var unique: String = "") extends Component {
  if (unique == "") unique = label
}
