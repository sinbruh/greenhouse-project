package no.ntnu.communication.messages;

import no.ntnu.communication.Message;

public class StateMessage extends Message {
  String nodeID;
  String actuatorID;
  String value;
  public StateMessage(String nodeID, String actuatorID, String value) {
    this.nodeID = nodeID;
    this.actuatorID = actuatorID;
    this.value = value;
  }

  @Override
  public String messageAsString() {
    return "state|" + nodeID + "|" + actuatorID + "|" + value;
  }
}
