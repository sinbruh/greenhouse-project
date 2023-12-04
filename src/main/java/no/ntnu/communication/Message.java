package no.ntnu.communication;
/**
 * Represents a message in the communication protocol.
 * This is an abstract class that should be extended by specific types of messages.
 */
public abstract class Message {

  /**
   * Converts this message into a string representation.
   * The specific format of the string depends on the type of the message.
   * @return The string representation of this message.
   */
  public abstract String messageAsString();
}
