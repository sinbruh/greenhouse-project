package no.ntnu.communication.commands;

import no.ntnu.communication.Command;
import no.ntnu.communication.Message;
import no.ntnu.communication.messages.ActuatorListMessage;
import no.ntnu.greenhouse.GreenhouseSimulator;
import no.ntnu.greenhouse.ActuatorCollection;
import no.ntnu.greenhouse.SensorActuatorNode;

import java.util.Map;


/**
 * Represents a command to get a list of actuators from a node.
 * Extends the Command class and implements the execute method and messageAsString method.
 */
public class GetListOfActuators extends Command {
  String nodeID;

  /**
   * Constructor for the GetListOfActuators class.
   * @param nodeID the ID of the node to get the list of actuators from.
   */
  public GetListOfActuators(String nodeID) {
    this.nodeID = nodeID;
  }

  /**
   * Executes the command to get a list of actuators from a node. Returns the number of
   * Actuators in the node.
   * @param simulator GreenhouseSimulator instance.
   * @return Message containing information about the number of actuators in the node.
   */
//  @Override
//  public Message execute(GreenhouseSimulator simulator) {
//    Map<Integer, SensorActuatorNode> nodes = simulator.getNodes();
//    if (nodes.containsKey(Integer.parseInt(nodeID))) {
//      ActuatorCollection actuators = nodes.get(Integer.parseInt(nodeID)).getActuators();
//      int size = actuators.size();
//      return new ActuatorListMessage("NumberOfActuators:" + size, nodeID);
//    } else {
//      return new ActuatorListMessage("NumberOfActuators:0", nodeID); //just a placement code
//    }
//  }


  @Override
  public Message execute(GreenhouseSimulator simulator) {
    return null;
  }

  /**
     * Returns the message as a string.
     * @return the message as a string.
     */
  @Override
  public String messageAsString() {
    return "getActuators";
  }
}
