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

  public SensorReadingMessage(String nodeid, List<Sensor> sensors) {
    this.sensors = sensors;
    this.nodeid = nodeid;
  }

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
