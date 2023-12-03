package no.ntnu.controlpanel;

import junit.framework.TestCase;
import org.junit.Test;

public class RealCommunicationChannelTest extends TestCase {

  @Test
  public void testParseSensorReadingMessage() {
    String message = "temperature:27.01:°C/temperature:27.13:°C/";
    RealCommunicationChannel channel = new RealCommunicationChannel(null);
    channel.parseSensorReading(message);
  }

}