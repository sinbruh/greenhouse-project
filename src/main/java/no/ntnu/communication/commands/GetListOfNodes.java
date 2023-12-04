package no.ntnu.communication.commands;

import java.util.Map;
import no.ntnu.communication.Command;
import no.ntnu.communication.Message;
import no.ntnu.communication.messages.ListOfNodesMessage;
import no.ntnu.greenhouse.GreenhouseSimulator;
import no.ntnu.greenhouse.SensorActuatorNode;

/**
 * Command to get a list of all nodes in the system. The list is sent as a ListOfNodesMessage.
 */
public class GetListOfNodes extends Command {

  /**
   * Execute the command. Returns a ListOfNodesMessage containing a list of all nodes in the system.
   * @param simulator GreenhouseSimulator instance
   * @return ListOfNodesMessage containing a
   */
  @Override
  public Message execute(GreenhouseSimulator simulator) {
    return new ListOfNodesMessage(simulator.getNodes().values());
  }

  /**
   * Returns the message as a string.
   * @return
   */
  @Override
  public String messageAsString() {
    return "getNodes";
  }
}
