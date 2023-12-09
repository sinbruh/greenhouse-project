package no.ntnu.communication.messages;

import java.util.List;
import no.ntnu.communication.Message;
import no.ntnu.greenhouse.Sensor;

/**
 * Message containing sensor readings.
 */
public class SensorReadingMessage extends Message {
  List<Sensor> sensors;
  String nodeid;

    /**
     * Constructs a new SensorReadingMessage with node-id and a list of sensors.
     *
     * @param nodeid  The id of the node.
     * @param sensors The list of sensors.
     */
  public SensorReadingMessage(String nodeid, List<Sensor> sensors) {
    this.sensors = sensors;
    this.nodeid = nodeid;
  }

    /**
     * Gets the list of sensors and their readings.
     *
     * @return The list of sensors.
     */
  @Override
  public String messageAsString() {
    StringBuilder builder = new StringBuilder();
    builder.append("sensorReading");
    builder.append("|");
    builder.append(nodeid);
    builder.append("|");

    for (Sensor sensor : sensors) {
      builder.append(sensor.getType());
      builder.append(":");
      builder.append(sensor.getReading().getValue());
      builder.append(":");
      builder.append(sensor.getReading().getUnit());
      builder.append("/");
    }

    return builder.toString();
  }
}
