package no.ntnu.communication.messages;

import java.util.List;
import no.ntnu.communication.Message;
import no.ntnu.greenhouse.Sensor;

public class SensorReadingMessage extends Message {
  List<Sensor> sensors;
  String nodeID;
  public SensorReadingMessage(String nodeID, List<Sensor> sensors) {
    this.sensors = sensors;
    this.nodeID = nodeID;
  }

  @Override
  public String messageAsString() {
    StringBuilder builder = new StringBuilder();
    builder.append("sensorReading");
    builder.append("|");
    builder.append(nodeID);
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
