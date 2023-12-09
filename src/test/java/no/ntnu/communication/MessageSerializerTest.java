package no.ntnu.communication;

import org.junit.Test;
import static org.junit.Assert.*;
import no.ntnu.communication.commands.*;
import no.ntnu.communication.messages.*;
public class MessageSerializerTest {

    @Test
    public void testCommandFromString() {
        // Test with a TurnOnCommand message
        String rawClientRequest = "on|132123|123123";
        Message message = MessageSerializer.fromString(rawClientRequest);
        assertTrue(message instanceof GetStateCommand);
    }

    @Test
    public void testMessageFromString() {
        // Test with a TemperatureMessage message
        String rawClientRequest = "temp|234234|234234|20";
        Message message = MessageSerializer.fromString(rawClientRequest);
        assertTrue(message instanceof TemperatureMessage);
    }
}
