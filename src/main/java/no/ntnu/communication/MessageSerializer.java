package no.ntnu.communication;

import no.ntnu.communication.commands.BroadCastSetStateCommand;
import no.ntnu.communication.commands.DisconnectCommand;
import no.ntnu.communication.commands.GetListOfNodeInfo;
import no.ntnu.communication.commands.SetStateCommand;
import no.ntnu.communication.messages.BroadCastStateMessage;
import no.ntnu.communication.messages.ListOfNodesMessage;
import no.ntnu.communication.messages.SensorReadingMessage;
import no.ntnu.communication.messages.StateMessage;
import no.ntnu.controlpanel.SensorActuatorNodeInfo;
import no.ntnu.greenhouse.Actuator;
import no.ntnu.greenhouse.NodeInterface;
import no.ntnu.greenhouse.SensorReading;
import no.ntnu.tools.Logger;
import no.ntnu.tools.Parser;
import java.util.ArrayList;
import java.util.Collection;
import java.util.zip.DataFormatException;

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
      case "setState" -> message = parseSetStateCommand(tokens[1], tokens[2], tokens[3]);
      case "getNodes" -> message = new GetListOfNodeInfo();
      case "setBroadcastState" -> message = parseBroadCastSetStateCommand(tokens[1], tokens[2]);
      case "disconnect" -> message = new DisconnectCommand();
      //messages
      case "state" -> message = parseStateMessage(tokens[1], tokens[2], tokens[3]);
      case "sensorReading" -> message = parseSensorReading(tokens[1], tokens[2]);
      case "nodes" -> message = parseNodesMessage(tokens);
      case "broadCastState" -> message = parseBroadcastStateMessage(tokens[1], tokens[2]);

      default -> message = new ErrorMessage("Invalid message type");
    }

    return message;
  }

  private static Message parseSetStateCommand(String nodeId, String actuatorId, String state) {
    SetStateCommand message = null;

    try {
      int parsedNodeId = Parser.parseIntegerOrError(nodeId, "Could not parse token");
      int parsedActuatorId = Parser.parseIntegerOrError(actuatorId, "Could not parse token");
      boolean parsedStateBool = Parser.parseBooleanOrError(state, "Could not parse token");
      message = new SetStateCommand(parsedNodeId, parsedActuatorId, parsedStateBool);
    } catch (DataFormatException e) {
      Logger.error("Could not parse state message");
    }

    return message;
  }

  private static Message parseNodesMessage(String[] tokens) {
    ListOfNodesMessage message = null;

    Collection<NodeInterface> nodeCollection = new ArrayList<>();

    for (int i = 1; i < tokens.length; i++) {
      Logger.info("Parsing node " + tokens[i]);


      String[] nodeTokens = tokens[i].split(":");
      int nodeId = Parser.parseIntegerOrError(nodeTokens[0],
              "Could not initialize nodes, invalid nodeid");
      SensorActuatorNodeInfo nodeInfo = new SensorActuatorNodeInfo(nodeId);
      if (nodeTokens.length > 1) {
        for (int j = 1; j < nodeTokens.length; j++) {
          String[] actuatorTokens = nodeTokens[j].split("/");
          nodeInfo.addActuator(
                  new Actuator(Parser.parseIntegerOrError(
                          actuatorTokens[0], "Could not initialize node, invalid actuatorid"),
                          actuatorTokens[1], nodeId));
          if (actuatorTokens.length > 2 && actuatorTokens[2].equals("on")) {
            nodeInfo.getActuator(Parser.parseIntegerOrError(
                    actuatorTokens[0], "Error: Could not parse actuator state")).set(true);
          } else if (actuatorTokens[2].equals("off")) {
            nodeInfo.getActuator(Parser.parseIntegerOrError(
                    actuatorTokens[0], "Error: Could not parse actuator state")).set(false);
          }
          Logger.info("Added actuator " + actuatorTokens[0] + " to node " + nodeId);
        }
      }
      nodeCollection.add(nodeInfo);
    }
    message = new ListOfNodesMessage(nodeCollection);

    return message;
  }

  private static Message parseSensorReading(String nodeId, String sensorReading) {
    Message message = null;

    Logger.info("Parsing sensor reading: " + sensorReading);
    ArrayList<SensorReading> sensorReadings = new ArrayList<>();
    String[] sensors = sensorReading.split("/");
    for (String sensor : sensors) {
      String[] sensorTokens = sensor.split(":");
      sensorReadings.add(
              new SensorReading(sensorTokens[0],
                      Double.parseDouble(sensorTokens[1]), sensorTokens[2]));
    }
    sensorReadings.forEach(SensorReading -> Logger.info(SensorReading.toString()));

    int parsedNodeId = Parser.parseIntegerOrError(nodeId, "Could not parse token");

    message = new SensorReadingMessage(parsedNodeId, sensorReadings);

    return message;
  }

  private static Message parseStateMessage(String nodeId, String actuatorId, String state) {
    StateMessage message = null;

    try {
      int parsedNodeId = Parser.parseIntegerOrError(nodeId, "Could not parse token");
      int parsedActuatorId = Parser.parseIntegerOrError(actuatorId, "Could not parse token");
      boolean parsedStateBool = Parser.parseBooleanOrError(state, "Could not parse token");
      message = new StateMessage(parsedNodeId, parsedActuatorId, parsedStateBool);
    } catch (DataFormatException e) {
      Logger.error("Could not parse state message");
    }

    return message;
  }

  private static Message parseBroadcastStateMessage(String nodeId, String state) {
    BroadCastStateMessage message = null;

    try {
      Logger.info("Parsing broadcast state message: " + nodeId + " " + state);
      int parsedNodeId = Parser.parseIntegerOrError(nodeId, "Could not parse token");
      boolean parsedStateBool = Parser.parseBooleanOrError(state, "Could not parse token");
      message = new BroadCastStateMessage(parsedNodeId, parsedStateBool);
    } catch (DataFormatException e) {
      Logger.error("Could not parse state message");
    }

    return message;
  }

  private static Command parseBroadCastSetStateCommand(String nodeId, String state) {
    BroadCastSetStateCommand command = null;

    try {
      int parsedNodeId = Parser.parseIntegerOrError(nodeId, "Could not parse token");
      boolean parsedStateBool = Parser.parseBooleanOrError(state, "Could not parse token");
      command = new BroadCastSetStateCommand(parsedNodeId, parsedStateBool);
    } catch (DataFormatException e) {
      Logger.error("Could not parse state command");
    }

    return command;
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
