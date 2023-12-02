package no.ntnu.communication.commands;

import no.ntnu.communication.Command;
import no.ntnu.communication.Message;
import no.ntnu.communication.messages.StateMessage;
import no.ntnu.greenhouse.Actuator;
import no.ntnu.greenhouse.SensorActuatorNode;

/**
 * Command class for toggling an Actuator. User types "Toggle"
 * to toggle it.
 */
public class Toggle extends Command {
  private String nodeID;
  private String actuatorID;

  public Toggle(String nodeID, String actuatorID) {
    this.nodeID = nodeID;
    this.actuatorID = actuatorID;
  }

  @Override
public Message execute() {
  Actuator actuator = new Actuator(actuatorID, Integer.parseInt(nodeID));
  actuator.toggle();
  return new StateMessage(nodeID, actuatorID, actuator.isOn() ? "on" : "off");
}

  @Override
  public String messageAsString() {
    return "ToggleCommand: NodeId=" + nodeID + ", ActuatorID=" + actuatorID;
  }
}