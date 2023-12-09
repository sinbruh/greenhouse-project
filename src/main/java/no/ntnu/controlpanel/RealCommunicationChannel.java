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
import no.ntnu.tools.Logger;

/**
 * A real communication channel. Communicates with the server over a TCP connection.
 */
public class RealCommunicationChannel extends Thread implements CommunicationChannel {
  private ControlPanelLogic logic;
  private PrintWriter socketWriter;
  private BufferedReader socketReader;
  private boolean isOpen;
    private Socket socket;

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
      int nodeId = Integer.parseInt(nodeTokens[0]);
      SensorActuatorNodeInfo nodeInfo = new SensorActuatorNodeInfo(nodeId);
      if (nodeTokens.length > 1) {
        for (int j = 1; j < nodeTokens.length; j++) {
          String[] actuatorTokens = nodeTokens[j].split("/");
          nodeInfo.addActuator(
              new Actuator(Integer.parseInt(actuatorTokens[0]), actuatorTokens[1], nodeId));
          if (actuatorTokens.length > 2 && actuatorTokens[2].equals("on")) {
            nodeInfo.getActuator(Integer.parseInt(actuatorTokens[0])).set(true);
          } else if (actuatorTokens[2].equals("off")) {
            nodeInfo.getActuator(Integer.parseInt(actuatorTokens[0])).set(false);
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
    boolean running = true;
    while (running) {
      String response = readResponse();
      String[] tokens = response.split("\\|");
      Logger.info("Received message: " + response);

      switch (tokens[0]) {
        case "sensorReading":
          logic.onSensorData(Integer.parseInt(tokens[1]), parseSensorReading(tokens[2]));
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
      running = !(response == null);
    }
  }

  public void parseStateMessage(String nodeid, String actuatorid, String state) {
    boolean stateBool = state.equals("on");
    logic.onActuatorStateChanged(Integer.parseInt(nodeid), Integer.parseInt(actuatorid), stateBool);
  }

  /**
   * Parses a broadcast state message.
   *
   * @param nodeid The node ID.
   * @param state  The state of the node.
   */
  public void parseBroadcastStateMessage(String nodeid, String state) {
    System.out.println("broadcaststate method");
    boolean stateBool = state.equals("on");
    logic.onAllActuatorChange(Integer.parseInt(nodeid), stateBool);
  }


  /**
   * Parses a sensor reading string into a list of SensorReading objects.
   *
   * @param sensorReading The sensor reading string to parse.
   * @return A list of SensorReading objects representing the parsed sensor readings.
   */
  public List<SensorReading> parseSensorReading(String sensorReading) {
    System.out.println("Parsing sensor reading: " + sensorReading);
    ArrayList<SensorReading> sensorReadings = new ArrayList<>();
    String[] sensors = sensorReading.split("/");
    for (String sensor : sensors) {
      String[] sensorTokens = sensor.split(":");
      sensorReadings.add(
          new SensorReading(sensorTokens[0], Double.parseDouble(sensorTokens[1]), sensorTokens[2]));
    }
    sensorReadings.forEach(System.out::println);
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
      System.err.println("Could not read response from server");
    }
    return response;
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
      System.err.println("could not initialize stream");
    }
  }

    /**
     * Closes the socket for this communication channel.
     */
    public void closeSocket() {
      try {
        if (socket !=null && !socket.isClosed()) {
          socketWriter.println("Disconnect");
            socket.close();
            isOpen = false;
            Logger.info("Socket closed");
        } else {
          Logger.info("Socket already closed");
        }
      } catch (IOException e) {
        Logger.error("Could not close socket" + e.getMessage());
      }
    }
}
