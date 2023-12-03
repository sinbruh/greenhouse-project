package no.ntnu.communication.messages;

import no.ntnu.communication.Message;

public class ActuatorListMessage extends Message {
  String listOfMessages;
  String actuatorIDs;

  public ActuatorListMessage(String listOfMessages, String actuatorIDs){
    super();
    this.listOfMessages = listOfMessages;
    this.actuatorIDs = actuatorIDs;
  }

  @Override
public String messageAsString() {
  StringBuilder builder = new StringBuilder();
  builder.append("actuators");
  String[] actuatorIDsArray = actuatorIDs.split(",");
  for (String actuatorID : actuatorIDsArray) {
    builder.append("|");
    builder.append(actuatorID);
  }
  return builder.toString();
}
}