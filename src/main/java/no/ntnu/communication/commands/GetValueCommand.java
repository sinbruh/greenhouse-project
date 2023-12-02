package no.ntnu.communication.commands;

import no.ntnu.communication.Command;
import no.ntnu.communication.Message;
import no.ntnu.greenhouse.GreenhouseSimulator;

public class GetValueCommand extends Command {
  public GetValueCommand(String nodeID, String sensorID) {
    super();
  }

  @Override
  public String messageAsString() {
    return null;
  }

  @Override
  public Message execute(GreenhouseSimulator simulator) {
    return null;
  }
}
