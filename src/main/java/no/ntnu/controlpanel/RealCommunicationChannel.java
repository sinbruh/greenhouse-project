package no.ntnu.controlpanel;

import java.io.IOException;
import java.io.PrintWriter;
import java.net.Socket;

public class RealCommunicationChannel implements CommunicationChannel {
  private Socket socket;
  private ControlPanelLogic logic;
  private PrintWriter socketWriter;
  private boolean isOpen;

  public RealCommunicationChannel(ControlPanelLogic logic) {
    this.logic = logic;
    isOpen = false;
  }
  @Override
  public void sendActuatorChange(int nodeId, int actuatorId, boolean isOn) {
    //TODO implement
  }

  public void testSend() {
    socketWriter.println("test");
  }

  @Override
  public boolean open() {
    return isOpen;
  }

  public void setSocket(Socket socket) {
    this.socket = socket;
    try {
      socketWriter = new PrintWriter(socket.getOutputStream(), true);
      isOpen = true;
    } catch (IOException e) {
      System.err.println("could not initialize stream");
    }
  }
}
