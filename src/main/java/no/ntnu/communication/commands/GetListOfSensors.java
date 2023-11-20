package no.ntnu.communication.commands;

import no.ntnu.communication.Command;
import no.ntnu.communication.Message;

/**
 * Class to recieve a list of the available sensors. It will not
 * require an ActuatorId or SensorId
 */
public class GetListOfSensors extends Command {
  public GetListOfSensors(String nodeID) {

  }

  @Override
  public Message execute() {
    return null;
  }

  @Override
  public String messageAsString() {
    return null;
  }
}
