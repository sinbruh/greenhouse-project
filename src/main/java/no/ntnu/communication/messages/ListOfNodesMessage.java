package no.ntnu.communication.messages;

import java.util.Collection;
import no.ntnu.communication.Message;
import no.ntnu.greenhouse.Actuator;
import no.ntnu.greenhouse.NodeInterface;

/**
 * Message class that will list all the nodes in the greenhouse.
 */
public class ListOfNodesMessage extends Message {
  Collection<NodeInterface> nodes;

  /**
   * Constructor for the ListOfNodesMessage class.
   *
   * @param nodes Collection of SensorActuatorNodes.
   */
  public ListOfNodesMessage(Collection<NodeInterface> nodes) {
    super();
    this.nodes = nodes;
  }

  public Collection<NodeInterface> getNodes() {
    return nodes;
  }

  /**
   * Converts the state of the SensorActuatorNode instance in the collection to
   * a formatted string that represents the node's "message" type that contains the
   * information about the nodes.
   *
   * @return a string representing the node's "message" type, containing info about the nodes.
   */
  @Override
  public String messageAsString() {
    StringBuilder builder = new StringBuilder();
    builder.append("nodes");

    for (NodeInterface nodeInfo : nodes) {
      builder.append("|");
      builder.append(nodeInfo.getId());
      for (Actuator actuator : nodeInfo.getActuators()) {
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
