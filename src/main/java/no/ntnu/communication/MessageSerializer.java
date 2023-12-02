package no.ntnu.communication;

import no.ntnu.communication.commands.*;
import no.ntnu.communication.messages.ActuatorListMessage;
import no.ntnu.communication.messages.HumidityMessage;
import no.ntnu.communication.messages.ListOfNodesMessage;
import no.ntnu.communication.messages.SensorListMessage;
import no.ntnu.communication.messages.StateMessage;
import no.ntnu.communication.messages.TemperatureMessage;

public class MessageSerializer {
  public static Message fromString(String rawClientRequest) {
    Message message = null;
    String[] tokens = rawClientRequest.split("\\|");
    switch (tokens[0]) {
      //Commands
      case "on" -> message = new TurnOnCommand(tokens[0], tokens[1], tokens[2]);
      case "off" -> message = new TurnOffCommand(tokens[0], tokens[1], tokens[2]);
      case "toggle" -> message = new Toggle(tokens[0], tokens[1]);
      case "getState" -> message = new GetStateCommand(tokens[0], tokens[1]);
      case "getSensors" -> message = new GetListOfSensors(tokens[0]);
      case "getActuators" -> message = new GetListOfActuators(tokens[0]);
      case "getNodes" -> message = new GetListOfNodes();
      case "getValue" -> message = new GetValueCommand(tokens[0], tokens[1]);
      //Messages
      case "temp" -> message = new TemperatureMessage(tokens[0], tokens[1], tokens[2]);
      case "humid" -> message = new HumidityMessage(tokens[0], tokens[1], tokens[2]);
      case "state" -> message = new StateMessage(tokens[0], tokens[1], tokens[2]);
      case "sensors" -> message = new SensorListMessage(tokens[0], tokens[1]);
      case "actuators" -> message = new ActuatorListMessage(tokens[0], tokens[1]);
//      case "nodes" -> message = new ListOfNodesMessage(tokens[0]);
    }

    return message;
  }

  public static String toString(Message response) {
    return response.messageAsString();
  }
}
