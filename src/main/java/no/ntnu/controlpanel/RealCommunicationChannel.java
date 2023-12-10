package no.ntnu.controlpanel;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import no.ntnu.communication.MessageSerializer;
import no.ntnu.communication.messages.BroadCastStateMessage;
import no.ntnu.communication.messages.ListOfNodesMessage;
import no.ntnu.communication.messages.SensorReadingMessage;
import no.ntnu.communication.messages.StateMessage;
import no.ntnu.server.GreenhouseServer;
import no.ntnu.tools.Logger;

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
   * Initializes nodes based on the provided response.
   * Each token represents a node and its actuators.
   *
   * @param response The response representing the nodes and their actuators.
   */
  public void initNodes(String response) {
    ListOfNodesMessage nodesMessage = (ListOfNodesMessage)
            MessageSerializer.fromString(response.toString());
    nodesMessage.getNodes().forEach(node -> {
      SensorActuatorNodeInfo sensorActuatorNodeInfo = new SensorActuatorNodeInfo(node.getId());
      node.getActuators().forEach(actuator -> sensorActuatorNodeInfo.addActuator(actuator));
      logic.onNodeAdded(sensorActuatorNodeInfo);
    });
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
          parseSensorReading(response);
          break;
        case "nodes":
          initNodes(response);
          break;
        case "state":
          parseStateMessage(response);
          break;
        case "broadCastState":
          parseBroadcastStateMessage(response);
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
   * @param response The response to parse.
   */
  public void parseStateMessage(String response) {
    StateMessage stateMessage = (StateMessage) MessageSerializer.fromString(response);
    logic.onActuatorStateChanged(stateMessage.getNodeid(),
            stateMessage.getActuatorid(), stateMessage.getState());
  }

  /**
   * Parses a broadcast state message.
   *
   * @param response The response to parse.
   */
  public void parseBroadcastStateMessage(String response) {
    Logger.info("rcc parsing: " + response);

    BroadCastStateMessage broadCastStateMessage =
            (BroadCastStateMessage) MessageSerializer.fromString(response);
    logic.onAllActuatorChange(broadCastStateMessage.getNodeid(), broadCastStateMessage.getState());
  }


  /**
   * Parses a sensor reading string into a list of SensorReading objects.
   *
   * @param sensorReading The sensor reading string to parse.
   */
  public void parseSensorReading(String sensorReading) {
    SensorReadingMessage sensorReadingMessage =
            (SensorReadingMessage) MessageSerializer.fromString(sensorReading);
    logic.onSensorData(sensorReadingMessage.getNodeid(), sensorReadingMessage.getSensorReadings());
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

  /**
   * Stops the execution of the current process and closing resources,
   * terminating the application.
   */
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
