package no.ntnu.communication.commands;

import no.ntnu.communication.Command;
import no.ntnu.communication.Message;

/**
 * Command class for turning on an Actuator. Typing "on" will
 * turn on an Actuator
 */
public class TurnOnCommand extends Command {
  private String nodeID;
  private String actuatorID;
  public TurnOnCommand(String nodeID, String actuatorID) {
    this.nodeID = nodeID;
    this.actuatorID = actuatorID;
  }


  @Override
  public Message execute() {
    return null;
  }

  @Override
  public String messageAsString() {
    return null;
  }
}
