package no.ntnu.communication.commands;

import no.ntnu.communication.Command;
import no.ntnu.communication.Message;
import no.ntnu.communication.messages.StateMessage;
import no.ntnu.greenhouse.Actuator;
import no.ntnu.greenhouse.GreenhouseSimulator;


/**
 * Command class for turning on an Actuator. Typing "on" will
 * turn on an Actuator
 */
public class TurnOnCommand extends Command {
  private String nodeID;
  private String actuatorID;
  private String actuatorType;

  /**
   * Constructor for the TurnOnCommand class.
   * @param nodeID ID of given node
   * @param actuatorID ID of given actuator
   * @param actuatorType type of actuator
   */
  public TurnOnCommand(String nodeID, String actuatorID, String actuatorType) {
    this.nodeID = nodeID;
    this.actuatorID = actuatorID;
  }

  /**
   * Executes the command to turn on an Actuator
   * @param simulator GreenhouseSimulator instance.
   * @return turn on message.
   */
  @Override
  public Message execute(GreenhouseSimulator simulator) {
    Actuator actuator = new Actuator(actuatorID, Integer.parseInt(nodeID));
    actuator.turnOn();
    return new StateMessage(nodeID, actuatorID, "on");
  }

  /**
   * Returns the message as a string.
   * @return the message as a string.
   */
  @Override
  public String messageAsString() {
    return "TurnOffCommand: NodeId=" + nodeID + "ActuatorId=" + actuatorID;
  }
}
