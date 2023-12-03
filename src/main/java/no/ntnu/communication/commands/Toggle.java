package no.ntnu.communication.commands;

import no.ntnu.communication.Command;
import no.ntnu.communication.Message;
import no.ntnu.communication.messages.StateMessage;
import no.ntnu.greenhouse.Actuator;
import no.ntnu.greenhouse.GreenhouseSimulator;

/**
 * Command class that will toggle an Actuator on or off. User types
 * "Toggle" to either turn on or off an Actuator
 */
public class Toggle extends Command {
  private String nodeID;
  private String actuatorID;

  /**
   * Constructor for the Toggle class.
   * @param nodeID ID of given node
   * @param actuatorID ID of given actuator
   */
  public Toggle(String nodeID, String actuatorID) {
    this.nodeID = nodeID;
    this.actuatorID = actuatorID;
  }

  /**
   * Executes the command to toggle an Actuator
   * and returns the state of the Actuator.
   * @param simulator
   * @return
   */
  @Override
  public Message execute(GreenhouseSimulator simulator) {
    Actuator actuator = new Actuator(actuatorID, Integer.parseInt(nodeID));
    actuator.toggle();
    return new StateMessage(nodeID, actuatorID, actuator.isOn() ? "on" : "off");
  }

  /**
   * Returns the message as a string.
   * @return the message as a string.
   */
  @Override
  public String messageAsString() {
    return "ToggleCommand: NodeId=" + nodeID + ", ActuatorID=" + actuatorID;
  }
}
