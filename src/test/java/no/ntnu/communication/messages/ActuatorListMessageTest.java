package no.ntnu.communication.messages;

import org.junit.Test;
import static org.junit.Assert.assertEquals;

public class ActuatorListMessageTest {

  @Test
  public void testMessageAsString() {
    String listOfMessages = "actuators";
    String actuatorIDs = "1,2,3";
    ActuatorListMessage message = new ActuatorListMessage(listOfMessages, actuatorIDs);
    String expected = "actuators|1|2|3";
    assertEquals(expected, message.messageAsString());
  }
}