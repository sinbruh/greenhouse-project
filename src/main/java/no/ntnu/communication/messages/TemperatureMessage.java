package no.ntnu.communication.messages;

import no.ntnu.communication.Message;

public class TemperatureMessage extends Message {
  public TemperatureMessage(String value, String nodeID, String sensorID) {
    super();
  }

  @Override
  public String messageAsString() {
    return null;
  }
}
