package no.ntnu.communication.messages;

import java.util.Collection;
import no.ntnu.communication.Message;
import no.ntnu.greenhouse.Actuator;
import no.ntnu.greenhouse.SensorActuatorNode;

/**
 * Message class that will list all the nodes in the greenhouse.
 */
public class ListOfNodesMessage extends Message {
  Collection<SensorActuatorNode> nodes;

  /**
   * Constructor for the ListOfNodesMessage class.
   *
   * @param nodes Collection of SensorActuatorNodes.
   */
  public ListOfNodesMessage(Collection<SensorActuatorNode> nodes) {
    super();
    this.nodes = nodes;
  }

  // nodes|node1:aid/atype/on:aid2/atype2|node2/off:id/type:id2/type2
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
        builder.append(actuator.getType());
        builder.append("/");
        builder.append(actuator.isOn() ? "on" : "off");
      }
    }
    return builder.toString();
  }
}
