package no.ntnu.communication.messages;

import no.ntnu.communication.Message;

public class ActuatorListMessage extends Message {
  Integer[] actuatorIDs;

  public ActuatorListMessage(Integer[] actuatorIDs){
    super();
    this.actuatorIDs = actuatorIDs;
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