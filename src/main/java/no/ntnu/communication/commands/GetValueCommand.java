package no.ntnu.communication.commands;

import no.ntnu.communication.Message;

public class GetValueCommand extends Message {
  public GetValueCommand(String nodeID, String sensorID) {
    super();
  }

  @Override
  public String messageAsString() {
    return null;
  }
}
