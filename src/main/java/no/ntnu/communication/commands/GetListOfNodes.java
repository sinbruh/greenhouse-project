package no.ntnu.communication.commands;

import no.ntnu.communication.Command;
import no.ntnu.communication.Message;

/**
 * Gives the user a list of the available Nodes, does not
 * require any type of ID
 */
public class GetListOfNodes extends Command {
  @Override
  public Message execute() {
    return null;
  }

  @Override
  public String messageAsString() {
    return null;
  }
}
