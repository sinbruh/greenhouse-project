package no.ntnu.communication.messages;

import no.ntnu.communication.Message;

public class ActuatorListMessage extends Message {
  Integer[] actuatorIDs;

  public ActuatorListMessage(String actuatorIDs){
    super();
    this.actuatorIDs = new Integer[actuatorIDs.length()];
  }

  @Override
  public String messageAsString() {
    StringBuilder builder = new StringBuilder();
    builder.append("actuators");
    for (Integer actuatorID : actuatorIDs) {
      builder.append("|");
      builder.append(actuatorID);
    }
    return builder.toString();
  }
}