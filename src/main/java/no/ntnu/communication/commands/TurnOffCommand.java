package no.ntnu.communication.commands;

import no.ntnu.communication.Command;
import no.ntnu.communication.Message;
import no.ntnu.communication.messages.StateMessage;
import no.ntnu.greenhouse.Actuator;
import no.ntnu.greenhouse.GreenhouseSimulator;

/**
 * Command class for turning off an Actuator. User types "Off"
 * to turn it off.
 */
public class TurnOffCommand extends Command {
  private String nodeID;
  private String actuatorID;
  private String actuatorType;


  public TurnOffCommand(String nodeID, String actuatorID, String actuatorType) {
    this.nodeID = nodeID;
    this.actuatorID = actuatorID;
  }

  @Override
  public Message execute(GreenhouseSimulator simulator) {
    Actuator actuator = new Actuator(actuatorID, Integer.parseInt(nodeID));
    actuator.turnOff();
    return new StateMessage(nodeID, actuatorID, "off");
  }

  @Override
  public String messageAsString() {
    return "TurnOffCommand: NodeId=" + nodeID + ", ActuatorID=" + actuatorID;
  }
}
