package no.ntnu.communication.commands;

import no.ntnu.communication.Command;
import no.ntnu.communication.Message;

/**
 * Command class for turning off an Actuator. User types "Off"
 * to turn it off.
 */
public class TurnOffCommand extends Command {
  private String nodeID;
  private String actuatorID;
  public TurnOffCommand(String nodeID, String actuatorID) {
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
