package no.ntnu.communication.messages;

import no.ntnu.communication.Message;

public class ListOfNodesMessage extends Message {
  String listOfMessages;
  String nodeIDs;

  public ListOfNodesMessage(String listOfMessages, String nodeIDs) {
    super();
    this.listOfMessages = listOfMessages;
    this.nodeIDs = nodeIDs;
  }

  @Override
  public String messageAsString() {
    StringBuilder builder = new StringBuilder();
    builder.append(listOfMessages);
    String[] nodeIDsArray = nodeIDs.split(",");
    for (String nodeID : nodeIDsArray) {
      builder.append("|");
      builder.append(nodeID);
    }
    return builder.toString();
  }
}