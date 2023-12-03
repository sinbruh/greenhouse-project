package no.ntnu.communication.commands;

import no.ntnu.communication.Message;
import no.ntnu.greenhouse.Sensor;
import no.ntnu.greenhouse.SensorActuatorNode;

/**
 * Represents a message to request the value of a specific sensor.
 * associated with a node in a GreenhouseSimulator.
 * It extends the Message class and provides a method to execute the command on a SensorActuatorNode.
 */
public class GetValueCommand extends Message {
  private String nodeID;
  private String sensorID;

  /**
   * Constructor for the GetValueCommand class.
   * @param nodeID ID of given node
   * @param sensorID ID of given sensor
   */
  public GetValueCommand(String nodeID, String sensorID) {
    this.nodeID = nodeID;
    this.sensorID = sensorID;
  }

  /**
   * Returns the result message as a string.
   * @return the result message as a string.
   */
  @Override
  public String messageAsString() {
    return "GetValueCommand: NodeId=" + nodeID + ", SensorID=" + sensorID;
  }

  /**
   * Executes the command to get the value of the given sensor,
   * and prints the value to the console.
   * @param node the node to execute the command on.
   */
  public void execute(SensorActuatorNode node) {
    Sensor sensor = node.getSensor(this.sensorID);;
    System.out.println("Sensor value: " + sensor.getReading().getValue());
  }
}