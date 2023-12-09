package no.ntnu.communication.commands;

import no.ntnu.communication.Command;
import no.ntnu.communication.ErrorMessage;
import no.ntnu.communication.Message;
import no.ntnu.communication.messages.StateMessage;
import no.ntnu.greenhouse.Actuator;
import no.ntnu.greenhouse.GreenhouseSimulator;
import no.ntnu.tools.Parser;

/**
 * Represents a command to set the state of an actuator, either on or off.
 * Extends the Command class and implements the execute method and messageAsString method.
 */
public class SetStateCommand extends Command {
  private final String nodeid;
  private final String actuatorid;
  private final String value;

  /**
   * Constructor for the SetStateCommand class.
   *
   * @param nodeid     ID of the node containing the actuator.
   * @param actuatorid ID of the actuator to set the state of.
   * @param value      The state to set the actuator to, either "on" or "off".
   */
  public SetStateCommand(String nodeid, String actuatorid, String value) {
    this.nodeid = nodeid;
    this.actuatorid = actuatorid;
    this.value = value;
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
    Actuator actuator = simulator.getNodes()
        .get(Parser.parseIntegerOrError(nodeid, "Could not execute command, node-id is invalid"))
        .getActuators()
        .get(Parser.parseIntegerOrError(actuatorid,
            "Could not execute command, actuator-id is invalid"));
    if (value.equals("on")) {
      actuator.turnOn();
    } else if (value.equals("off")) {
      actuator.turnOff();
    } else {
      return new ErrorMessage("error: invalid value in setState command");
    }
    return new StateMessage(Parser.parseIntegerOrError(nodeid, "Could not parse node-id"),
        Parser.parseIntegerOrError(actuatorid, "Could not parse actuator-id"),
        actuator.isOn());
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
