package no.ntnu.communication.messages;

import no.ntnu.communication.Message;

/**
 * Message class that will broadcast the state of all actuators on a node.
 */
public class BroadCastStateMessage extends Message {
  private final int nodeid;
  private final boolean state;

  /**
   * Constructor for the BroadCastStateMessage class. Represents a broadcast
   * state message
   * @param nodeid ID of the node.
   * @param state State of the actuators on the node.
   */
  public BroadCastStateMessage(int nodeid, boolean state) {
    this.nodeid = nodeid;
    this.state = state;
  }

  public int getNodeid() {
    return nodeid;
  }

  public boolean getState() {
    return state;
  }

    /**
     * Returns the message as a string suited for the broadcasting state message.
     * @return the message as a string.
     */
  @Override
  public String messageAsString() {
    return "broadCastState|" + nodeid + "|" + (state ? "on" : "off");
  }
}
