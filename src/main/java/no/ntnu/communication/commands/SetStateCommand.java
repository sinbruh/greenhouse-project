package no.ntnu.communication.commands;

import no.ntnu.communication.Command;
import no.ntnu.communication.ErrorMessage;
import no.ntnu.communication.Message;
import no.ntnu.communication.messages.StateMessage;
import no.ntnu.greenhouse.Actuator;
import no.ntnu.greenhouse.GreenhouseSimulator;

public class SetStateCommand extends Command {
    private String nodeID;
    private String actuatorID;
    private String value;
    public SetStateCommand(String nodeID, String actuatorID, String value) {
        this.nodeID = nodeID;
        this.actuatorID = actuatorID;
    }

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

    @Override
    public String messageAsString() {
        return "SetStateCommand: NodeId=" + nodeID + ", ActuatorID=" + actuatorID;
    }
}
