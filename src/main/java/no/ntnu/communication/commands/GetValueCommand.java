package no.ntnu.communication.commands;

import no.ntnu.communication.Message;
import no.ntnu.greenhouse.Sensor;
import no.ntnu.greenhouse.SensorActuatorNode;

public class GetValueCommand extends Message {
  private String nodeID;
  private String sensorID;

  public GetValueCommand(String nodeID, String sensorID) {
    this.nodeID = nodeID;
    this.sensorID = sensorID;
  }

  @Override
  public String messageAsString() {
    return "GetValueCommand: NodeId=" + nodeID + ", SensorID=" + sensorID;
  }

  public void execute(SensorActuatorNode node) {
    Sensor sensor = node.getSensor(Integer.parseInt(this.sensorID));
    System.out.println("Sensor value: " + sensor.getReading().getValue());
  }
}