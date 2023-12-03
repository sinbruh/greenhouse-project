package no.ntnu.controlpanel;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;
import no.ntnu.communication.commands.GetListOfNodes;
import no.ntnu.greenhouse.Actuator;
import no.ntnu.greenhouse.DeviceFactory;
import no.ntnu.greenhouse.Sensor;
import no.ntnu.greenhouse.SensorReading;

public class RealCommunicationChannel extends Thread implements CommunicationChannel {
  private Socket socket;
  private ControlPanelLogic logic;
  private PrintWriter socketWriter;
  private BufferedReader socketReader;
  private boolean isOpen;

  public RealCommunicationChannel(ControlPanelLogic logic) {
    this.logic = logic;
    isOpen = false;
  }
  @Override
  public void sendActuatorChange(int nodeId, int actuatorId, boolean isOn) {
    //TODO implement
  }

  public void initNodes(String[] tokens) {
    for (int i = 1; i < tokens.length; i++) {
      System.out.println("Adding node " + tokens[i]);
      logic.onNodeAdded(new SensorActuatorNodeInfo(Integer.parseInt(tokens[i])));
    }
  }

  public void sendGetNodesCommand() {
    socketWriter.println("getNodes");
  }

  @Override
  public void run() {
    //TODO complete implementation
    boolean running = true;
    while (running) {
      String response = readResponse();
      String[] tokens = response.split("\\|");
      System.out.println("Received message: " + response);

      switch (tokens[0]) {
        case "sensorReading" : logic.onSensorData(Integer.parseInt(tokens[1]), parseSensorReading(tokens[2]));
          break;
        case "nodes" : initNodes(tokens);
      }

      running = readResponse() != null;
    }
  }

  public List<SensorReading> parseSensorReading(String sensorReading) {
    System.out.println("Parsing sensor reading: " + sensorReading);
    ArrayList<SensorReading> sensorReadings = new ArrayList<>();
    String[] sensors = sensorReading.split("/");
    for (String sensor : sensors) {
      String[] sensorTokens = sensor.split(":");
      sensorReadings.add(new SensorReading
          (sensorTokens[0], Double.parseDouble(sensorTokens[1]), sensorTokens[2]));
    }
    sensorReadings.forEach(System.out::println);
    return sensorReadings;
  }

  @Override
  public boolean open() {
    return isOpen;
  }

  public String readResponse() {
    String response = null;
    try {
      response = socketReader.readLine();
    } catch (IOException e) {
      System.err.println("Could not read response from server");
    }
    return response;
  }

  public void setSocket(Socket socket) {
    this.socket = socket;
    try {
      socketWriter = new PrintWriter(socket.getOutputStream(), true);
      socketReader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
      isOpen = true;
    } catch (IOException e) {
      System.err.println("could not initialize stream");
    }
  }
}
