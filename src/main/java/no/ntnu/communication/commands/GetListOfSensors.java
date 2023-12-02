package no.ntnu.communication.commands;

import no.ntnu.communication.Command;
import no.ntnu.communication.Message;
import no.ntnu.greenhouse.GreenhouseSimulator;

/**
 * Class to recieve a list of the available sensors. It will not
 * require an ActuatorId or SensorId
 */
public class GetListOfSensors extends Command {
  public GetListOfSensors(String nodeID) {

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
