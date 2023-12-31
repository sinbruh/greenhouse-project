package no.ntnu.communication.commands;

import no.ntnu.communication.Command;
import no.ntnu.communication.Message;
import no.ntnu.communication.messages.StateMessage;
import no.ntnu.greenhouse.Actuator;
import no.ntnu.greenhouse.GreenhouseSimulator;

/**
 * Represents a command to set the state of an actuator, either on or off.
 * Extends the Command class and implements the execute method and messageAsString method.
 */
public class SetStateCommand extends Command {
  private final int nodeid;
  private final int actuatorid;
  private final boolean state;

  /**
   * Constructor for the SetStateCommand class.
   *
   * @param nodeid     ID of the node containing the actuator.
   * @param actuatorid ID of the actuator to set the state of.
   * @param state      The state to set the actuator to, either "on" or "off".
   */
  public SetStateCommand(int nodeid, int actuatorid, boolean state) {
    this.nodeid = nodeid;
    this.actuatorid = actuatorid;
    this.state = state;
  }

  /**
   * Executes the command to set the state of an actuator.
   * Returns a StateMessage containing the new state of the actuator.
   *
   * @param simulator The GreenhouseSimulator to execute this command on.
   * @return StateMessage containing the new state of the actuator.
   */
  @Override
  public Message execute(GreenhouseSimulator simulator) {
    Actuator actuator = simulator.getNodes().get(nodeid).getActuators().get(actuatorid);
    if (state) {
      actuator.turnOn();
    } else {
      actuator.turnOff();
    }
    return new StateMessage(nodeid, actuatorid, actuator.isOn());
  }

  /**
   * Returns the message as a string.
   *
   * @return the message as a string.
   */
  @Override
  public String messageAsString() {
    return "SetStateCommand: NodeId=" + nodeid + ", ActuatorID=" + actuatorid;
  }
}
