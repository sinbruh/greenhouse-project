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

  public GetStateCommand(String nodeID, String actuatorID) {
    this.nodeID = nodeID;
    this.actuatorID = actuatorID;
  }

  @Override
  public Message execute(GreenhouseSimulator simulator) {
    Actuator actuator = new Actuator(actuatorID, Integer.parseInt(nodeID));
    return new StateMessage(nodeID, actuatorID, actuator.isOn() ? "on" : "off");
  }

  @Override
  public String messageAsString() {
    return "GetStateCommand: NodeId=" + nodeID + ", ActuatorID=" + actuatorID;
  }
}