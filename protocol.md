# Communication protocol

This document describes the protocol used for communication between the different nodes of the
distributed application.

## Terminology

* Sensor - a device which senses the environment and describes it with a value (an integer value in
  the context of this project). Examples: temperature sensor, humidity sensor.
* Actuator - a device which can influence the environment. Examples: a fan, a window opener/closer,
  door opener/closer, heater.
* Sensor and actuator node - a computer which has direct access to a set of sensors, a set of
  actuators and is connected to the Internet.
* Control-panel node - a device connected to the Internet which visualizes status of sensor and
  actuator nodes and sends control commands to them.
* Graphical User Interface (GUI) - A graphical interface where users of the system can interact with
  it.

## The underlying transport protocol

The project uses TCP for handling communication. The port number of the TCP server 1025. We chose TCP because it is
reliable and connection oriented. We want to make sure that all the messages are received and that the messages are
received in the correct order. We also want to make sure that the messages are received by the correct client. TCP
handles all of this for us.

## The architecture

The architecture consists of a central server that connects to multiple control panels. The server will also
talk to the greenhouse simulator, but all the networking is done on the server not the greenhouse simulator. The
greenhouse simulator is where all the sensor and actuator nodes are located.

## The flow of information and events

After a connection has been established between a control panel and the server, the control panel will send a request
for the nodes on the network. The server will then send a list of all the nodes on the network. After this, the server
starts sending sensor data to the control panel whenever it is updated. The control panel will then display this data.

The control panel can send a command to a specific node, multicast or broadcast. The server will then execute the 
command on the greenhouse. After the command has been executed, the server will send a response to all control panels,
updating them on the current state of the nodes. Each client handler acts as a listener to the nodes, so whenever a node
is changed, the client handler will be notified and send the updated data to the control panels.


## Connection and state

The communication is connection oriented, as we're using TCP for the transport protocol. The server will keep track of
all the controls panels that are connected to it. Each control panel will be handled in a separate thread. When a 
control panel sends a request for a list of all the nodes, the server will send this, then also start sending 
sensor data to the control panel. The server sends sensor data whenever it is updated. 

## Types, constants

 * Boolean - 1 or 0, represents true or false.
 * NodeID - integer, represents the ID of a node.
 * ActuatorID - integer, represents the ID of an actuator.
 * commandType - string, represents the type of command that is sent to a node.
 * messageValue - a value, can be a sensor reading or a state of an actuator.

## Message format

There are two types of messages, a regular message and a command. The commands will only be sent from the control panels
and will generally have a type (what the command does), a nodeID (which node gets affected), and an actuatorID. Some
commands don't need all this information though, so they might not have all of these fields. 

We will use the symbol "|" as a separator. So an example message could look like this "turnOn|4561|1". If the command 
should be broadcasted, then we use a B symbol instead of the nodeID; like this: "turnOn|B". The structure of a message 
will be similar to the structure of a command.

Commands: "commandType|nodeID|actuatorID/sensorID|value|"
* setState - sets the state of an actuator, on or off. type: "setState"
* getListOfSensors - returns a list of sensors does not require an actuatorId or sensorID. type: "getSensors"
* getListOfNodes - returns a list of all nodes. type: "getNodes"

Messages: "messageType|messageValue|nodeID|sensorID"
* state - the state of an actuator, can be on or off. type: "state"
* listOfSensors - returns a list of sensor. The list will be in the form of a string and needs to be parsed.
    The separator used in the string to separate sensors is a colon ":". type: "sensors"
* listOfNodeInfo - returns a list of nodes. The list will be in the form of a string and needs to be parsed.
    The separator used in the string to separate nodes is a colon ":". type: "nodes"
* successful operation - when a command is sent to a sensor-actuator node, a successful operation will be sent to
    the control panel that sent the command. type: "so"
* error - when a command is sent to a sensor-actuator node, but an error occurs, an error message will be sent to
    the control panel that sent the command. type: "error"
* SensorReadingMessage - a message that contains a list of sensor readings on a node. The list will be formatted like
    this "type:value:unit/type:value:unit". ":" separates the values of a sensor, "/" separates
    sensors. the sensor type can be humidity or temperature (currently). type: "sensorReading"

### Error messages
 

* Too many arguments.
* Invalid ID.
* Invalid argument.
* Not enough arguments.

## An example scenario

1. A sensor node with ID=1 is started. It has a temperature sensor, two humidity sensors. It can
   also open a window.
2. A sensor node with ID=2 is started. It has a single temperature sensor and can control two fans
   and a heater.
3. A control panel node is started.
4. Another control panel node is started.
5. A sensor node with ID=3 is started. It has a two temperature sensors and no actuators.
6. After 5 seconds all three sensor/actuator nodes broadcast their sensor data.
7. The user of the first-control panel presses on the button "ON" for the first fan of
   sensor/actuator node with ID=2.
8. The user of the second control-panel node presses on the button "turn off all actuators".

## Reliability and security

TODO - describe the reliability and security mechanisms your solution supports.

