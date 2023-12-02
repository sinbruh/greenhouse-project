package no.ntnu.communication.commands;

import no.ntnu.communication.Command;
import no.ntnu.communication.Message;
import no.ntnu.greenhouse.GreenhouseSimulator;

/**
 * Command class that will toggle an Actuator on or off. User types
 * "Toggle" to either turn on or off an Actuator
 */
public class Toggle extends Command {
  public Toggle(String nodeID, String actuatorID) {
    super();
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
