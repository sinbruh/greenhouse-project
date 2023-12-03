package no.ntnu.communication.commands;

import no.ntnu.communication.Command;
import no.ntnu.communication.ErrorMessage;
import no.ntnu.communication.Message;
import no.ntnu.greenhouse.GreenhouseSimulator;
import no.ntnu.greenhouse.Sensor;
import no.ntnu.greenhouse.SensorActuatorNode;

/**
 * Class to receive a list of the available sensors. It will not
 * require an ActuatorId or SensorId
 */
public class GetListOfSensors extends Command {
    String sensorNodeID;
  public GetListOfSensors(String sensorNodeID) {
    this.sensorNodeID = sensorNodeID;
  }

  @Override
  public Message execute(GreenhouseSimulator simulator) {
    SensorActuatorNode node = simulator.getNodes().get(Integer.parseInt(sensorNodeID));
    if (node != null) {
      StringBuilder builder = new StringBuilder();
      builder.append("sensors");
      for (Sensor sensor : node.getSensors()) {
        builder.append("|");
        builder.append(sensor.getSensorNodeId());
      }
      return new ErrorMessage(builder.toString());
    } else {
      return new ErrorMessage("error");
    }
  }

  @Override
  public String messageAsString() {
    return "getSensors";
  }
}
