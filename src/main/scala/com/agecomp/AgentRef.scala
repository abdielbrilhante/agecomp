package com.agecomp

import akka.actor._

class AgentRef(val props: Props) extends Component {
  var actor: ActorRef = null
  var flag = "Create"
}
