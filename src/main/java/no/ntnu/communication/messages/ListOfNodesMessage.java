package no.ntnu.communication.messages;

import java.util.Collection;
import no.ntnu.communication.Message;
import no.ntnu.greenhouse.Actuator;
import no.ntnu.greenhouse.SensorActuatorNode;

public class ListOfNodesMessage extends Message {
  Collection<SensorActuatorNode> nodes;
  public ListOfNodesMessage(Collection<SensorActuatorNode> nodes) {
    super();
    this.nodes = nodes;
  }

  // nodes|node1:aid/atype:aid2/atype2|node2:id/type:id2/type2
  @Override
  public String messageAsString() {
    StringBuilder builder = new StringBuilder();
    builder.append("nodes");
    for (SensorActuatorNode node : nodes) {
      builder.append("|");
      builder.append(node.getId());
      for (Actuator actuator : node.getActuators()) {
        builder.append(":");
        builder.append(actuator.getId());
        builder.append("/");

      }
    }
    return builder.toString();
  }
}
