package no.ntnu.communication.commands;

import no.ntnu.communication.Command;
import no.ntnu.communication.Message;
import no.ntnu.greenhouse.GreenhouseSimulator;

public class GetStateCommand extends Command {
  public GetStateCommand(String nodeID, String actuatorID) {
  }

  @Override
  public Message execute(GreenhouseSimulator simulator) {
    return null;
  }

  @Override
  public String messageAsString() {
    return null;
  }
}
