package no.ntnu.communication.messages;

import no.ntnu.communication.Message;

public class StateMessage extends Message {
  int nodeID;
  int actuatorID;
  boolean value;
  public StateMessage(int nodeID, int actuatorID, boolean value) {
    this.nodeID = nodeID;
    this.actuatorID = actuatorID;
    this.value = value;
  }

  @Override
  public String messageAsString() {
    return "state|" + nodeID + "|" + actuatorID + "|" + (value ? "on" : "off");
  }
}
