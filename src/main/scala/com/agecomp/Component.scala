package com.agecomp

class Component {
  private var _id = -1

  def id = _id

  def id_= (value: Int):Unit = { _id = value }
}

class InputComponent extends Component
