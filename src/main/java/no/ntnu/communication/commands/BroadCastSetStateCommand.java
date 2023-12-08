package no.ntnu.communication.commands;


import no.ntnu.communication.Command;
import no.ntnu.communication.Message;
import no.ntnu.communication.messages.BroadCastStateMessage;
import no.ntnu.greenhouse.GreenhouseSimulator;

public class BroadCastSetStateCommand extends Command {
  private String nodeID;
  private String state;
  public BroadCastSetStateCommand(String nodeID, String state) {
    this.nodeID = nodeID;
    this.state = state;
  }

  @Override
  public String messageAsString() {
    return null;
  }

  @Override
  public Message execute(GreenhouseSimulator simulator) {
    simulator.getNodes().get(Integer.parseInt(nodeID)).getActuators().forEach(actuator -> actuator.set(state.equals("on")));
    return new BroadCastStateMessage(nodeID, state);
  }
}
