package no.ntnu.communication.commands;

import no.ntnu.communication.Command;
import no.ntnu.communication.Message;

/**
 * Gives the user a list of the available Actuators, does not
 * require any type of ID
 */
public class GetListOfActuators extends Command {
  String nodeID;
  public GetListOfActuators(String nodeID) {
    this.nodeID = nodeID;
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
