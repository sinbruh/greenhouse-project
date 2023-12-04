package no.ntnu.communication.commands;

import no.ntnu.communication.Command;
import no.ntnu.communication.ErrorMessage;
import no.ntnu.communication.Message;
import no.ntnu.communication.messages.StateMessage;
import no.ntnu.greenhouse.Actuator;
import no.ntnu.greenhouse.GreenhouseSimulator;

/**
 * Represents a command to set the state of an actuator, either on or off.
 * Extends the Command class and implements the execute method and messageAsString method.
 */
public class SetStateCommand extends Command {
    private String nodeID;
    private String actuatorID;
    private String value;

    /**
     * Constructor for the SetStateCommand class.
     * @param nodeID ID of the node containing the actuator.
     * @param actuatorID ID of the actuator to set the state of.
     * @param value The state to set the actuator to, either "on" or "off".
     */
    public SetStateCommand(String nodeID, String actuatorID, String value) {
        this.nodeID = nodeID;
        this.actuatorID = actuatorID;
    }

    /**
     * Executes the command to set the state of an actuator.
     * Returns a StateMessage containing the new state of the actuator.
     * @param simulator The GreenhouseSimulator to execute this command on.
     * @return StateMessage containing the new state of the actuator.
     */
    @Override
    public Message execute(GreenhouseSimulator simulator) {
        Actuator actuator = simulator.getNodes().get(Integer.parseInt(nodeID)).getActuators().get(Integer.parseInt(actuatorID));
        if (value.equals("on")) {
            actuator.turnOn();
        } else if (value.equals("off")){
            actuator.turnOff();
        } else {
            return new ErrorMessage("error");
        }
        return new StateMessage(nodeID, actuatorID, actuator.isOn() ? "on" : "off");
    }

    /**
     * Returns the message as a string.
     * @return the message as a string.
     */
    @Override
    public String messageAsString() {
        return "SetStateCommand: NodeId=" + nodeID + ", ActuatorID=" + actuatorID;
    }
}
