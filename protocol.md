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

TODO - what transport-layer protocol do you use? TCP? UDP? What port number(s)? Why did you 
choose this transport layer protocol?

The project uses TCP for handling communication. The port number of the TCP server 1025.

## The architecture
The architecture consists of a central server that connects to multiple nodes and control panels. The server will also
talk to the greenhouse simulator, but all the networking is done on the server not the greenhouse simulator.

[Server Architecture](images/GreenhouseServerArchitecture.png)

## The flow of information and events

TODO - describe what each network node does and when. Some periodic events? Some reaction on 
incoming packets? Perhaps split into several subsections, where each subsection describes one 
node type (For example: one subsection for sensor/actuator nodes, one for control panel nodes).

Commands are sent from a control panel, the command can be to a specific sensor node, multicasted or broadcasted.
The nodes can also send messages, these will be either single-casted or broadcasted. When a command is sent, the
actuator will perform the command and a response will be sent. The response depends on the command that was sent. If
for example on control panel wants to know whether an actuator is on or not, then the response does not need to be sent
to all control panels. If the command that control panel sends is to actually turn it on or off, then all control panels
should be notified.

## Connection and state

TODO - is your communication protocol connection-oriented or connection-less? Is it stateful or 
stateless? 
The communication is connection oriented, as we're using TCP for the transport protocol.

## Types, constants

TODO - Do you have some specific value types you use in several messages? They you can describe 
them here.

## Message format

TODO - describe the general format of all messages. Then describe specific format for each 
message type in your protocol.

There are two types of messages, a regular message and a command. The commands will only be sent from the control panels
and will have a type (what the command does), a nodeID (which node gets affected).

We will use the symbol "|" as a separator. So an example message could look like this "turnOn|4561". If the command 
should be broadcasted, then we use a B symbol instead of the nodeID; like this: "turnOn|B". The structure of a message 
will be similar to the structure of a command. 

Commands: "commandType|nodeID|actuatorID/sensorID|"
* turnOn - turns on an actuator. type: "on"
* turnOff - turns off an actuator. type: "off"
* toggle - toggles an actuator on or off. type "toggle"
* getState - returns the state of an actuator, on or off. type: "getState"
* getValue - gets the value of a sensor. type: "getValue"
* getListOfSensors - returns a list of sensors does not require an actuatorId or sensorID. type: "getSensors"
* getListOfActuators - returns a list of actuators, does not require an actuatorId or sensorID. type: "getActuators"
* getListOfNodes - returns a list of all nodes. type: "getNodes"

Messages: "messageType|messageValue|nodeID|sensorID"
* temperature - the temperature of a sensor node in Celsius. type: "temp"
* humidity - the humidity of a sensor node. type: "humid"
* state - the state of an actuator, can be on or off. type: "state"
* listOfSensors - returns a list of sensor. The list will be in the form of a string and needs to be parsed.
    The separator used in the string to separate sensors is a colon ":". type: "sensors"
* listOfActuators - returns a list of actuators. The list will be in the form of a string and needs to be parsed.
    The separator used in the string to separate actuators is a colon ":". type: "actuators"
* listOfNodes - returns a list of nodes. The list will be in the form of a string and needs to be parsed.
    The separator used in the string to separate nodes is a colon ":". type: "nodes"
* successful operation - when a command is sent to a sensor-actuator node, a successful operation will be sent to
    the control panel that sent the command. type: "so"
* error - when a command is sent to a sensor-actuator node, but an error occurs, an error message will be sent to
    the control panel that sent the command. type: "error"

### Error messages

TODO - describe the possible error messages that nodes can send in your system.

* Too many arguments.
* Invalid ID.
* Invalid argument.
* Not enough arguments.

## An example scenario

TODO - describe a typical scenario. How would it look like from communication perspective? When 
are connections established? Which packets are sent? How do nodes react on the packets? An 
example scenario could be as follows:
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
