package no.ntnu.communication.commands;

import no.ntnu.communication.Command;
import no.ntnu.communication.ErrorMessage;
import no.ntnu.communication.Message;
import no.ntnu.greenhouse.GreenhouseSimulator;
import no.ntnu.greenhouse.Sensor;
import no.ntnu.greenhouse.SensorActuatorNode;

/**
 * Represents a command to retrieve a list of sensors from a node.
 * Extends the Command class and implements the execute method and messageAsString method.
 *
 */
public class GetListOfSensors extends Command {
    String sensorNodeID;

  /**
   * Constructor for the GetListOfSensors class.
   * @param sensorNodeID ID of sensor-node
   */
  public GetListOfSensors(String sensorNodeID) {
    this.sensorNodeID = sensorNodeID;
  }

  /**
   * Executes the command to retrieve a list of sensors from a node. Returns the number of
   * sensors in the given node.
   * @param simulator GreenhouseSimulator instance.
   * @return Message containing information about the number of sensors in the node.
   */
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

  /**
   * Returns the message as a string.
   * @return the message as a string.
   */
  @Override
  public String messageAsString() {
    return "getSensors";
  }
}
