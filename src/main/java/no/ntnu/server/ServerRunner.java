package no.ntnu.server;

import no.ntnu.greenhouse.GreenhouseSimulator;

import java.io.IOException;

/**
 * Main class for the server.
 */
public class ServerRunner {
    /**
     * Starts the server
     *
     * @param args the argument to the program.
     */
    public static void main(String[] args) throws IOException {
        GreenhouseSimulator simulator = new GreenhouseSimulator(false);
        GreenhouseServer server = new GreenhouseServer(simulator);

        server.startServer(GreenhouseServer.CONTROL_PANEL_PORT);
    }
}