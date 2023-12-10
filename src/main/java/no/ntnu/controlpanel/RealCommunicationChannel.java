package no.ntnu.controlpanel;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;
import no.ntnu.greenhouse.Actuator;
import no.ntnu.greenhouse.SensorReading;
import no.ntnu.server.GreenhouseServer;
import no.ntnu.tools.Logger;
import no.ntnu.tools.Parser;

/**
 * A real communication channel. Communicates with the server over a TCP connection.
 */
public class RealCommunicationChannel extends Thread implements CommunicationChannel {
  protected static final int MAX_RECONNECT_ATTEMPTS = 5;
  private static final long RECONNECT_DELAY_MS = 5000;
  private Socket socket;
  private ControlPanelLogic logic;
  private PrintWriter socketWriter;
  private BufferedReader socketReader;
  private boolean isOpen;
  private boolean isRunning;

  public RealCommunicationChannel(ControlPanelLogic logic) {
    this.logic = logic;
    isOpen = false;
  }

  @Override
  public void sendActuatorChange(int nodeId, int actuatorId, boolean isOn) {
    Logger.info("Sent: " + "setState|" + nodeId + "|" + actuatorId + "|" + (isOn ? "on" : "off"));
    socketWriter.println("setState|" + nodeId + "|" + actuatorId + "|" + (isOn ? "on" : "off"));
  }

  public void sendBroadcastStateCommand(int nodeid, boolean state) {
    Logger.info("setBroadcastState|" + nodeid + "|" + (state ? "on" : "off"));
    socketWriter.println("setBroadcastState|" + nodeid + "|" + (state ? "on" : "off"));
  }

  /**
   * Initializes nodes based on the provided tokens. Each token represents a node and its actuators.
   *
   * @param tokens The tokens representing the nodes and their actuators.
   */
  public void initNodes(String[] tokens) {
    for (int i = 1; i < tokens.length; i++) {
      Logger.info("Adding node " + tokens[i]);


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

      logic.onNodeAdded(nodeInfo);
    }
  }

  /**
   * Sends a command to get the list of nodes.
   */
  public void sendGetNodesCommand() {
    socketWriter.println("getNodes");
  }

  /**
   * Continuously reads responses from the server and handles them based on their type.
   */
  @Override
  public void run() {
    isRunning = true;
    while (isRunning) {
      String response = readResponse();

      if ((response == null) && isRunning) {
        reconnect();
        response = readResponse();
        if (response == null) {
          isRunning = false;
          break;
        }
      }

      String[] tokens = response.split("\\|");
      Logger.info("Received message: " + response);

      switch (tokens[0]) {
        case "sensorReading":
          logic.onSensorData(Parser.parseIntegerOrError(tokens[1], "Could not parse token"),
              parseSensorReading(tokens[2]));
          break;
        case "nodes":
          initNodes(tokens);
          break;
        case "state":
          parseStateMessage(tokens[1], tokens[2], tokens[3]);
          break;
        case "broadCastState":
          parseBroadcastStateMessage(tokens[1], tokens[2]);
          break;
        default:
          Logger.error("Unknown message type: " + tokens[0]);
          break;
      }
    }
  }

  private void reconnect() {
    for (int attempt = 1; attempt <= MAX_RECONNECT_ATTEMPTS; attempt++) {
      try {
        Thread.sleep(RECONNECT_DELAY_MS);
        initializeStreams(new Socket("localhost", GreenhouseServer.CONTROL_PANEL_PORT));
        sendGetNodesCommand();
        isOpen = true;
        Logger.info("Reconnected to the server on attempt " + attempt);
        return;
      } catch (IOException e) {
        Logger.error("Reconnection attempt " + attempt + " failed");
      } catch (InterruptedException e) {
        Logger.error("Reconnection attempt " + attempt + " interrupted");
        Thread.currentThread().interrupt();
      }
    }
    Logger.error("Failed to reconnect after " + MAX_RECONNECT_ATTEMPTS + " attempts");

  }

  /**
   * Parses a state message.
   *
   * @param nodeid     The node ID.
   * @param actuatorid The actuator ID.
   * @param state      The state of the actuator.
   */
  public void parseStateMessage(String nodeid, String actuatorid, String state) {
    boolean stateBool = state.equals("on");
    logic.onActuatorStateChanged(Parser.parseIntegerOrError(nodeid,
            "Could not parse nodeid in statemessage"),
        Parser.parseIntegerOrError(actuatorid, "Could not parse nodeid in statemessage"),
        stateBool);
  }

  /**
   * Parses a broadcast state message.
   *
   * @param nodeid The node ID.
   * @param state  The state of the node.
   */
  public void parseBroadcastStateMessage(String nodeid, String state) {
    Logger.info("broadcaststate method");
    boolean stateBool = state.equals("on");
    logic.onAllActuatorChange(Parser.parseIntegerOrError(nodeid,
            "Could not parse nodeid in parseBroadCastMessage"),
        stateBool);
  }


  /**
   * Parses a sensor reading string into a list of SensorReading objects.
   *
   * @param sensorReading The sensor reading string to parse.
   * @return A list of SensorReading objects representing the parsed sensor readings.
   */
  public List<SensorReading> parseSensorReading(String sensorReading) {
    Logger.info("Parsing sensor reading: " + sensorReading);
    ArrayList<SensorReading> sensorReadings = new ArrayList<>();
    String[] sensors = sensorReading.split("/");
    for (String sensor : sensors) {
      String[] sensorTokens = sensor.split(":");
      sensorReadings.add(
          new SensorReading(sensorTokens[0], Double.parseDouble(sensorTokens[1]), sensorTokens[2]));
    }
    sensorReadings.forEach(SensorReading -> Logger.info(SensorReading.toString()));
    return sensorReadings;
  }

  /**
   * Checks if the communication channel is open.
   *
   * @return true if the communication channel is open, false otherwise.
   */
  @Override
  public boolean open() {
    return isOpen;
  }

  /**
   * Reads a response from the server.
   *
   * @return The response from the server, or null if an error occurred while reading the response.
   */
  public String readResponse() {
    String response = null;
    try {
      response = socketReader.readLine();
    } catch (IOException e) {
      Logger.info("Could not read response from server");
    }
    return response;
  }

  public void stopRunning() {
    isRunning = false;
    isOpen = false;
    closeSocket();
    System.exit(0);
  }

  /**
   * Initializes the input and output streams for this communication channel.
   *
   * @param socket The socket to set for this communication channel.
   */
  public void initializeStreams(Socket socket) {
    try {
      this.socket = socket;
      socketWriter = new PrintWriter(socket.getOutputStream(), true);
      socketReader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
      isOpen = true;
    } catch (IOException e) {
      Logger.info("could not initialize stream");
    }
  }

  /**
   * Closes the socket for this communication channel.
   */
  public void closeSocket() {
    try {
      socketWriter.println("disconnect");

      if (socket != null && !socket.isClosed() && isOpen) {
        socketWriter.println("Disconnect");
        socket.close();
        isOpen = false;
      }
        Logger.info("Socket closed");
    } catch (IOException e) {
      Logger.error("Could not close socket" + e.getMessage());
    }
  }

  protected void setSocketWriter(PrintWriter writer) {
    this.socketWriter = writer;
}
}
