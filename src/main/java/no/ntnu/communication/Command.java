package no.ntnu.communication;

import no.ntnu.greenhouse.GreenhouseSimulator;

/**
 * Represents a command in the communication protocol.
 * This is an abstract class that extends the Message class and should be extended by specific types of commands.
 */
public abstract class Command extends Message {

  /**
   * Executes this command on a given GreenhouseSimulator.
   * The specific behavior of this method depends on the type of the command.
   *
   * @param simulator The GreenhouseSimulator to execute this command on.
   * @return A Message object representing the result of executing this command.
   */
  public abstract Message execute(GreenhouseSimulator simulator);
}
