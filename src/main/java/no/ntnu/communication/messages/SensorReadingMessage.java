package no.ntnu.communication.messages;

import java.util.ArrayList;
import java.util.List;
import no.ntnu.communication.Message;
import no.ntnu.greenhouse.Sensor;
import no.ntnu.greenhouse.SensorReading;

/**
 * Message containing sensor readings.
 */
public class SensorReadingMessage extends Message {
  List<SensorReading> sensorReadings;
  int nodeid;

  /**
   * Constructs a SensorReadingMessage object with the specified list of sensors and node ID.
   *
   * @param sensors A list of Sensor objects from which readings will be extracted.
   * @param nodeId The unique identifier for the node associated with the sensor readings.
   */
  public SensorReadingMessage(List<Sensor> sensors, int nodeId) {
    this.nodeid = nodeId;
    this.sensorReadings = new ArrayList<>();
    for (Sensor sensor : sensors) {
      sensorReadings.add(sensor.getReading());
    }
  }

  public int getNodeid() {
    return nodeid;
  }

  /**
   * Constructs a new SensorReadingMessage with node-id and a list of sensors.
   *
   * @param nodeid  The id of the node.
   * @param sensors The list of sensors.
   */
  public SensorReadingMessage(int nodeid, List<SensorReading> sensors) {
    this.sensorReadings = sensors;
    this.nodeid = nodeid;
  }

  public List<SensorReading> getSensorReadings() {
    return sensorReadings;
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


    for (SensorReading sensorReading : sensorReadings) {
      builder.append(sensorReading.getType());
      builder.append(":");
      builder.append(sensorReading.getValue());
      builder.append(":");
      builder.append(sensorReading.getUnit());
      builder.append("/");
    }

    return builder.toString();
  }
}
