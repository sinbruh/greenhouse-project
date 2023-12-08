package no.ntnu.communication.commands;

import no.ntnu.communication.Command;
import no.ntnu.communication.Message;
import no.ntnu.communication.messages.StateMessage;
import no.ntnu.greenhouse.Actuator;
import no.ntnu.greenhouse.GreenhouseSimulator;

/**
 * Command class that will get the state of an Actuator. User types
 * "GetState" to retrieve the state of an Actuator
 */
public class GetStateCommand extends Command {
  private String nodeID;
  private String actuatorID;

  /**
   * Constructor for the GetStateCommand class.
   * @param nodeID ID of given node
   * @param actuatorID ID of given actuator
   */
  public GetStateCommand(String nodeID, String actuatorID) {
    this.nodeID = nodeID;
    this.actuatorID = actuatorID;
  }

  /**
   * Executes the command to get the state of an Actuator. Returns the state of the Actuator.
   * @param simulator GreenhouseSimulator instance.
   * @return Message containing information about the state of the Actuator.
   */
  @Override
  public Message execute(GreenhouseSimulator simulator) {
    Actuator actuator = new Actuator(actuatorID, Integer.parseInt(nodeID));
    return new StateMessage(Integer.parseInt(nodeID), Integer.parseInt(actuatorID), actuator.isOn());
  }

  /**
   * Returns the message as a string.
   * @return the message as a string.
   */
  @Override
  public String messageAsString() {
    return "GetStateCommand: NodeId=" + nodeID + ", ActuatorID=" + actuatorID;
  }
}