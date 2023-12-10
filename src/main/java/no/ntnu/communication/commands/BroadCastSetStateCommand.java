package no.ntnu.communication.commands;


import no.ntnu.communication.Command;
import no.ntnu.communication.Message;
import no.ntnu.communication.messages.BroadCastStateMessage;
import no.ntnu.greenhouse.GreenhouseSimulator;
import no.ntnu.tools.Parser;

/**
 * Command class that will set the state of an all actuators on a node.
 */
public class BroadCastSetStateCommand extends Command {
  private final int nodeid;
  private final boolean state;

  public BroadCastSetStateCommand(int nodeid, boolean state) {
    this.nodeid = nodeid;
    this.state = state;
  }

  @Override
  public String messageAsString() {
    return null;
  }

  @Override
  public Message execute(GreenhouseSimulator simulator) {
    simulator.getNodes().get(nodeid).getActuators().forEach(actuator -> actuator.set(state));
    return new BroadCastStateMessage(nodeid, state);
  }
}
