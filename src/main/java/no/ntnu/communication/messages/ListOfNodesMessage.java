package no.ntnu.communication.messages;

import no.ntnu.communication.Message;

public class ListOfNodesMessage extends Message {
  Integer[] nodeIDs;
  public ListOfNodesMessage(Integer[] nodeIDs) {
    super();
    this.nodeIDs = nodeIDs;
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
