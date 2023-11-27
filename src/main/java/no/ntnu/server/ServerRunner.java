package no.ntnu.server;

import no.ntnu.greenhouse.GreenhouseSimulator;

public class ServerRunner {
    public static void main(String[] args) {
        GreenhouseSimulator simulator = new GreenhouseSimulator(false);
        GreenhouseServer server = new GreenhouseServer(simulator);

        server.startServer(GreenhouseServer.CONTROL_PANEL_PORT, GreenhouseServer.NODE_PORT);
    }
}