package no.ntnu.communication.messages;

import no.ntnu.communication.Message;

/**
 * Message class that will broadcast the state of all actuators on a node.
 */
public class BroadCastStateMessage extends Message {
  private final String nodeid;
  private final String state;

  public BroadCastStateMessage(String nodeid, String state) {
    this.nodeid = nodeid;
    this.state = state;
  }

  @Override
  public String messageAsString() {
    return "broadCastState|" + nodeid + "|" + state;
  }
}
