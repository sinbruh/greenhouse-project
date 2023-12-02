package no.ntnu.communication.commands;

import no.ntnu.communication.Command;
import no.ntnu.communication.Message;
import no.ntnu.communication.messages.ActuatorListMessage;
import no.ntnu.greenhouse.GreenhouseSimulator;
import no.ntnu.greenhouse.ActuatorCollection;
import no.ntnu.greenhouse.SensorActuatorNode;
import no.ntnu.messages.ErrorMessage;

import java.util.Map;


/**
 * Gives the user a list of the available Actuators, does not
 * require any type of ID
 */
public class GetListOfActuators extends Command {
  String nodeID;
  public GetListOfActuators(String nodeID) {
    this.nodeID = nodeID;
  }
  @Override
  public Message execute(GreenhouseSimulator simulator) {
    Map<Integer, SensorActuatorNode> nodes = simulator.getNodes();
    if (nodes.containsKey(Integer.parseInt(nodeID))) {
      ActuatorCollection actuators = nodes.get(Integer.parseInt(nodeID)).getActuators();
      int size = actuators.size();
      return new ActuatorListMessage("NumberOfActuators:" + size, nodeID);
    } else {
      return new ErrorMessage("Node not found");
    }
  }

  @Override
  public String messageAsString() {
    return "getActuators";
  }
}
