package no.ntnu.communication.messages;

import no.ntnu.communication.Message;

public class BroadCastStateMessage extends Message {
  private String nodeID;
  private String state;
  public BroadCastStateMessage(String nodeID, String state) {
    this.nodeID = nodeID;
    this.state = state;
  }

  @Override
  public String messageAsString() {
    return "broadCastState|" + nodeID + "|" + state;
  }
}
