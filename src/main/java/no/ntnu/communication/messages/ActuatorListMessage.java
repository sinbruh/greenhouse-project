package no.ntnu.communication.messages;

import no.ntnu.communication.Message;

public class ActuatorListMessage extends Message {
  Integer[] nodeIDs;
  public ActuatorListMessage(String listOfMessages, String nodeID){
    super();
  }

  @Override
  public String messageAsString() {
    StringBuilder builder = new StringBuilder();
    builder.append("nodes");
    for (Integer nodeID : nodeIDs) {
      builder.append("|");
      builder.append(nodeID);
    }
    return builder.toString();
  }
}
