package no.ntnu.communication.commands;


import no.ntnu.communication.Command;
import no.ntnu.communication.Message;
import no.ntnu.communication.messages.BroadCastStateMessage;
import no.ntnu.greenhouse.GreenhouseSimulator;

public class BroadCastSetStateCommand extends Command {
  private final String nodeid;
  private final String state;

  public BroadCastSetStateCommand(String nodeid, String state) {
    this.nodeid = nodeid;
    this.state = state;
  }

  @Override
  public String messageAsString() {
    return null;
  }

  @Override
  public Message execute(GreenhouseSimulator simulator) {
    simulator.getNodes().get(Integer.parseInt(nodeid)).getActuators().forEach(
        actuator -> actuator.set(state.equals("on")));
    return new BroadCastStateMessage(nodeid, state);
  }
}