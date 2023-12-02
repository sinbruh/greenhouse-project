package no.ntnu.communication.commands;

import no.ntnu.communication.Message;

public class GetStateCommand extends Message {
  public GetStateCommand(String nodeID, String actuatorID) {
    super();
  }

  @Override
  public String messageAsString() {
    return null;
  }
}
