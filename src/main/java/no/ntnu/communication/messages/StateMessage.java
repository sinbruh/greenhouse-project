package no.ntnu.communication.messages;

import no.ntnu.communication.Message;

/**
 * Message containing the state of an actuator.
 */
public class StateMessage extends Message {
  int nodeid;
  int actuatorid;
  boolean value;

  /**
   * Constructor for the StateMessage class.
   *
   * @param nodeid The ID of the node containing the actuator.
   * @param actuatorid The ID of the actuator.
   * @param value The state the actuator should be set to.
   */
  public StateMessage(int nodeid, int actuatorid, boolean value) {
    this.nodeid = nodeid;
    this.actuatorid = actuatorid;
    this.value = value;
  }

    /**
     * Converts the state info into a formatted string.
     * The format represents the state of the actuator.
     *
     * @return The state of the actuator.
     */
  @Override
  public String messageAsString() {
    return "state|" + nodeid + "|" + actuatorid + "|" + (value ? "on" : "off");
  }
}
