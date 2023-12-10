package no.ntnu.communication;

import org.junit.Test;
import static org.junit.Assert.*;
import no.ntnu.communication.commands.*;
import no.ntnu.communication.messages.*;
public class MessageSerializerTest {

    @Test
    public void testCommandFromString() {
        // Test with a TurnOnCommand message
        String rawClientRequest = "setState|234234|234234|on";
        Message message = MessageSerializer.fromString(rawClientRequest);
        assertTrue(message instanceof SetStateCommand);
    }

    @Test
    public void testMessageFromString() {
        // Test with a TemperatureMessage message
        String rawClientRequest = "state|234234|234234|on";
        Message message = MessageSerializer.fromString(rawClientRequest);
        assertTrue(message instanceof StateMessage);
    }
}
