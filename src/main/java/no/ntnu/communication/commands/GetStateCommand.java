package no.ntnu.communication.commands;

import no.ntnu.communication.Command;
import no.ntnu.communication.Message;
import no.ntnu.communication.messages.StateMessage;
import no.ntnu.greenhouse.Actuator;
import no.ntnu.greenhouse.GreenhouseSimulator;
import no.ntnu.tools.Parser;

/**
 * Command class that will get the state of an Actuator. User types
 * "GetState" to retrieve the state of an Actuator
 */
public class GetStateCommand extends Command {
  private final String nodeid;
  private final String actuatorid;

  /**
   * Constructor for the GetStateCommand class.
   *
   * @param nodeid ID of given node
   * @param actuatorid ID of given actuator
   */
  public GetStateCommand(String nodeid, String actuatorid) {
    this.nodeid = nodeid;
    this.actuatorid = actuatorid;
  }

  /**
   * Executes the command to get the state of an Actuator. Returns the state of the Actuator.
   *
   * @param simulator GreenhouseSimulator instance.
   * @return Message containing information about the state of the Actuator.
   */
  @Override
  public Message execute(GreenhouseSimulator simulator) {
    Actuator actuator = new Actuator(actuatorid, Parser.parseIntegerOrError(
        nodeid, "Could not execute command, invalid nodeid"));
    return new StateMessage(
        Parser.parseIntegerOrError(nodeid, "Could not execute, nodeid is invalid"),
        Parser.parseIntegerOrError(actuatorid, "Could not execute, actuatorid is invalid"),
        actuator.isOn());
  }

  /**
   * Returns the message as a string.
   *
   * @return the message as a string.
   */
  @Override
  public String messageAsString() {
    return "GetStateCommand: NodeId=" + nodeid + ", ActuatorID=" + actuatorid;
  }
}