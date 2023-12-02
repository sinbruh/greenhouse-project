package no.ntnu.communication.commands;

import java.util.Map;
import no.ntnu.communication.Command;
import no.ntnu.communication.Message;
import no.ntnu.communication.messages.ListOfNodesMessage;
import no.ntnu.greenhouse.GreenhouseSimulator;
import no.ntnu.greenhouse.SensorActuatorNode;

/**
 * Gives the user a list of the available Nodes, does not
 * require any type of ID
 */
public class GetListOfNodes extends Command {

  @Override
  public Message execute(GreenhouseSimulator simulator) {
    return new ListOfNodesMessage(simulator.getNodes().keySet().toArray(new Integer[0]));
  }

  @Override
  public String messageAsString() {
    return "getNodes";
  }
}
