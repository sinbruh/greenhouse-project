package no.ntnu.communication.commands;

import no.ntnu.communication.Command;
import no.ntnu.communication.Message;
import no.ntnu.greenhouse.GreenhouseSimulator;

/**
 * Command class that will toggle an Actuator on or off. User types
 * "Toggle" to either turn on or off an Actuator
 */
public class Toggle extends Command {
  int nodeID;
  int actuatorID;
  public Toggle(String nodeID, String actuatorID) {
    this.nodeID = Integer.parseInt(nodeID);
    this.actuatorID = Integer.parseInt(actuatorID);
  }

  @Override
  public Message execute(GreenhouseSimulator simulator) {
    simulator.getNodes().get(nodeID).getActuators().get(actuatorID).toggle();
    return null;
  }

  @Override
  public String messageAsString() {
    return null;
  }
}
