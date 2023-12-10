package no.ntnu.run;

import no.ntnu.greenhouse.GreenhouseSimulator;
import no.ntnu.server.GreenhouseServer;

/**
 * Main class for the server.
 */
public class GreenhouseStarter {
  /**
   * Starts the server.
   *
   * @param args the argument to the program.
   */
  public static void main(String[] args) {
    GreenhouseSimulator simulator = new GreenhouseSimulator(false);
    GreenhouseServer server = new GreenhouseServer(simulator);

    server.startServer(GreenhouseServer.CONTROL_PANEL_PORT);
  }
}