package no.ntnu.communication;

import no.ntnu.communication.commands.BroadCastSetStateCommand;
import no.ntnu.communication.commands.DisconnectCommand;
import no.ntnu.communication.commands.GetListOfNodeInfo;
import no.ntnu.communication.commands.GetStateCommand;
import no.ntnu.communication.commands.SetStateCommand;

/**
 * Class for serializing and deserializing Message objects.
 */
public class MessageSerializer {

  private MessageSerializer() {
    //Empty constructor
  }

  /**
   * Deserializes a string into a Message object.
   * The type of the Message object is determined by the first token in the string.
   *
   * @param rawClientRequest The string to deserialize.
   * @return The deserialized Message object, or null if the string could not be deserialized.
   */
  public static Message fromString(String rawClientRequest) {
    Message message = null;
    String[] tokens = rawClientRequest.split("\\|");
    switch (tokens[0]) {
      //Commands
      case "setState" -> message = new SetStateCommand(tokens[1], tokens[2], tokens[3]);
      case "getState" -> message = new GetStateCommand(tokens[0], tokens[1]);
      case "getNodes" -> message = new GetListOfNodeInfo();
      case "setBroadcastState" -> message = new BroadCastSetStateCommand(tokens[1], tokens[2]);
      case "disconnect" -> message = new DisconnectCommand();
      default -> message = new ErrorMessage("Invalid message type");
    }

    return message;
  }

  /**
   * Serializes a Message object into a string.
   *
   * @param response The Message object to serialize.
   * @return The serialized Message object as a string.
   */
  public static String toString(Message response) {
    return response.messageAsString();
  }
}
