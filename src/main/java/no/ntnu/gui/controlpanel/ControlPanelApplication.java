package no.ntnu.gui.controlpanel;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.Tab;
import javafx.scene.control.TabPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import no.ntnu.controlpanel.CommunicationChannel;
import no.ntnu.controlpanel.ControlPanelLogic;
import no.ntnu.controlpanel.RealCommunicationChannel;
import no.ntnu.controlpanel.SensorActuatorNodeInfo;
import no.ntnu.greenhouse.Actuator;
import no.ntnu.greenhouse.SensorReading;
import no.ntnu.gui.common.ActuatorPane;
import no.ntnu.gui.common.SensorPane;
import no.ntnu.listeners.common.ActuatorListener;
import no.ntnu.listeners.common.CommunicationChannelListener;
import no.ntnu.listeners.controlpanel.GreenhouseEventListener;
import no.ntnu.tools.Logger;

/**
 * Run a control panel with a graphical user interface (GUI), with JavaFX.
 */
public class ControlPanelApplication extends Application implements GreenhouseEventListener,
        CommunicationChannelListener, ActuatorListener {
  private static ControlPanelLogic logic;
  private static final int WIDTH = 500;
  private static final int HEIGHT = 400;
  private static CommunicationChannel channel;

  private TabPane nodeTabPane;
  private Scene mainScene;
  private final Map<Integer, SensorPane> sensorPanes = new HashMap<>();
  private final Map<Integer, ActuatorPane> actuatorPanes = new HashMap<>();
  private final Map<Integer, SensorActuatorNodeInfo> nodeInfos = new HashMap<>();
  private final Map<Integer, Tab> nodeTabs = new HashMap<>();

  /**
   * Application entrypoint for the GUI of a control panel.
   * Note - this is a workaround to avoid problems with JavaFX not finding the modules!
   * We need to use another wrapper-class for the debugger to work.
   *
   * @param logic   The logic of the control panel node
   * @param channel Communication channel for sending control commands and receiving events
   */
  public static void startApp(ControlPanelLogic logic, CommunicationChannel channel) {
    if (logic == null) {
      throw new IllegalArgumentException("Control panel logic can't be null");
    }
    ControlPanelApplication.logic = logic;
    ControlPanelApplication.channel = channel;
    Logger.info("Running control panel GUI...");
    launch();
  }

  /**
   * Method to start the control panel stage.
   *
   * @param stage The control panel stage to start.
   */
  @Override
  public void start(Stage stage) {
    if (channel == null) {
      throw new IllegalStateException(
              "No communication channel. See the README on how to use fake event spawner!");
    }

    stage.setMinWidth(WIDTH);
    stage.setMinHeight(HEIGHT);
    stage.setTitle("Control panel");
    mainScene = new Scene(createEmptyContent(), WIDTH, HEIGHT);
    stage.setScene(mainScene);
    stage.show();
    logic.addListener(this);
    logic.setCommunicationChannelListener(this);
    if (!channel.open()) {
      logic.onCommunicationChannelClosed();
    }
  }

  private static Pane createEmptyContent() {
    Label l = new Label("Waiting for node data...");
    l.setAlignment(Pos.CENTER);

    Button requestNodesButton = new Button("Request nodes");
    requestNodesButton.setOnAction(event -> channel.sendGetNodesCommand());

    VBox content = new VBox(l, requestNodesButton);
    content.setAlignment(Pos.CENTER);
    content.setSpacing(10);

    return content;
  }

  /**
   * This method gives a callback when a new
   * sensorActuatorNode is added to the system.
   *
   * @param nodeInfo Information about the added node.
   */
  @Override
  public void onNodeAdded(SensorActuatorNodeInfo nodeInfo) {
    System.out.println("Node added: " + nodeInfo.getId());
    Platform.runLater(() -> addNodeTab(nodeInfo));
  }

  /**
   * Callback method that initiates when a
   * sensorActuatorNode is removed.
   *
   * @param nodeId ID of the node which has disappeared (removed)
   */
  @Override
  public void onNodeRemoved(int nodeId) {
    Tab nodeTab = nodeTabs.get(nodeId);
    if (nodeTab != null) {
      Platform.runLater(() -> {
        removeNodeTab(nodeId, nodeTab);
        forgetNodeInfo(nodeId);
        if (nodeInfos.isEmpty()) {
          removeNodeTabPane();
        }
      });
      Logger.info("Node " + nodeId + " removed");
    } else {
      Logger.error("Can't remove node " + nodeId + ", there is no Tab for it");
    }
  }

  /**
   * Removes the node tab scene from the main scene.
   */
  private void removeNodeTabPane() {
    mainScene.setRoot(createEmptyContent());
    nodeTabPane = null;
  }

  /**
   * Callback method that is initiates when sensor data is received from a node.
   *
   * @param nodeId  ID of the node
   * @param sensors List of all current sensor values
   */
  @Override
  public void onSensorData(int nodeId, List<SensorReading> sensors) {
    Logger.info("Sensor data from node " + nodeId);
    SensorPane sensorPane = sensorPanes.get(nodeId);

    if (sensorPane != null) {
      sensorPane.update(sensors);
    } else {
      sensorPanes.put(nodeId, new SensorPane(sensors));
      Logger.error("No sensor section for node " + nodeId);
    }
  }

  /**
   * Callback method that initiates when an actuator changes on a node.
   *
   * @param nodeId     ID of the node to which the actuator is attached.
   * @param actuatorId ID of the actuator whose state changes.
   * @param isOn       When true, actuator is on; off when false.
   */
  @Override
  public void onActuatorStateChanged(int nodeId, int actuatorId, boolean isOn) {
    String state = isOn ? "ON" : "off";
    Logger.info("actuator[" + actuatorId + "] on node " + nodeId + " is " + state);
    ActuatorPane actuatorPane = actuatorPanes.get(nodeId);
    if (actuatorPane != null) {
      Actuator actuator = getStoredActuator(nodeId, actuatorId);
      if (actuator != null) {
        if (isOn) {
          actuator.turnOn();
        } else {
          actuator.turnOff();
        }
        actuatorPane.update(actuator);
      } else {
        Logger.error(" actuator not found");
      }
    } else {
      Logger.error("No actuator section for node " + nodeId);
    }
  }

  /**
   * Callback method that invokes when the state of all
   * the actuators on a node change.
   *
   * @param nodeid ID of the node where all actuators are located.
   * @param isOn   Indicating whether all actuators are in the on state.
   */
  @Override
  public void onAllActuatorChange(int nodeid, boolean isOn) {
    for (Actuator actuator : nodeInfos.get(nodeid).getActuators()) {
      Logger.info("actuator[" + actuator.getId() + "] on node " + nodeid + " is " + isOn);
      onActuatorStateChanged(nodeid, actuator.getId(), isOn);
    }
  }

  /**
   * Retrieves the stored actuators.
   *
   * @param nodeId     ID of the node.
   * @param actuatorId ID of the actuator.
   * @return Returns the corresponding actuator to the specified node.
   */
  private Actuator getStoredActuator(int nodeId, int actuatorId) {
    Actuator actuator = null;
    SensorActuatorNodeInfo nodeInfo = nodeInfos.get(nodeId);
    if (nodeInfo != null) {
      actuator = nodeInfo.getActuator(actuatorId);
    }
    return actuator;
  }

  /**
   * Removes information about a node.
   *
   * @param nodeId ID of the node to forget.
   */
  private void forgetNodeInfo(int nodeId) {
    sensorPanes.remove(nodeId);
    actuatorPanes.remove(nodeId);
    nodeInfos.remove(nodeId);
  }

  /**
   * Removes a node tab identified by its ID from the nodeTabs.
   *
   * @param nodeId  The ID of the node whose tab is to be removed.
   * @param nodeTab The Tab object representing the node tab to be removed.
   */
  private void removeNodeTab(int nodeId, Tab nodeTab) {
    nodeTab.getTabPane().getTabs().remove(nodeTab);
    nodeTabs.remove(nodeId);
  }

  /**
   * Adds a new node tab to the TabPane in the GUI,
   * creating a new SensorActuatorNodeInfo if it does not exist.
   *
   * @param nodeInfo The sensorActuatorNode representing information.
   */
  private void addNodeTab(SensorActuatorNodeInfo nodeInfo) {
    if (nodeTabPane == null) {
      nodeTabPane = new TabPane();
      mainScene.setRoot(nodeTabPane);
    }
    Tab nodeTab = nodeTabs.get(nodeInfo.getId());
    if (nodeTab == null) {
      nodeInfos.put(nodeInfo.getId(), nodeInfo);
      nodeTabPane.getTabs().add(createNodeTab(nodeInfo));
    } else {
      Logger.info("Duplicate node spawned, ignore it");
    }
  }

  /**
   * Creates a new Tab for a node with the specified SensorActuatorNodeInfo.
   *
   * @param nodeInfo The SensorActuatorNodeInfo representing information about the node.
   * @return The created tab object.
   */
  private Tab createNodeTab(SensorActuatorNodeInfo nodeInfo) {
    SensorPane sensorPane = createEmptySensorPane();
    sensorPanes.put(nodeInfo.getId(), sensorPane);
    ActuatorPane actuatorPane = new ActuatorPane(nodeInfo.getActuators());
    actuatorPane.addActuatorListener(this);
    actuatorPanes.put(nodeInfo.getId(), actuatorPane);
    HBox toolbar = createToolBar(nodeInfo);

    Tab tab = new Tab("Node " + nodeInfo.getId());
    tab.setContent(new VBox(sensorPane, actuatorPane, toolbar));
    nodeTabs.put(nodeInfo.getId(), tab);
    return tab;
  }

  /**
   * Creates a toolbar for controlling all actuators.
   *
   * @param nodeInfo The SensorActuatorNodeInfo representing information about the node.
   * @return The created HBox representing the toolbar.
   */
  private HBox createToolBar(SensorActuatorNodeInfo nodeInfo) {
    HBox toolBar = new HBox();
    toolBar.setSpacing(10);
    toolBar.setAlignment(Pos.CENTER);
    Button allOnButton = new Button("All on");
    Button allOffButton = new Button("All off");
    allOnButton.setOnAction(event -> channel.sendBroadcastStateCommand(nodeInfo.getId(), true));
    allOffButton.setOnAction(event -> channel.sendBroadcastStateCommand(nodeInfo.getId(), false));
    toolBar.getChildren().addAll(allOnButton, allOffButton);

    Button disconnectButton = new Button("Disconnect");
    disconnectButton.setOnAction(event -> {
      if (channel instanceof RealCommunicationChannel realCommunicationChannel) {
        realCommunicationChannel.closeSocket();
      }
    });
    toolBar.getChildren().add(disconnectButton);
    return toolBar;
  }

  /**
   * Creates an empty sensorPane.
   *
   * @return An instance of sensorPane.
   */
  private static SensorPane createEmptySensorPane() {
    return new SensorPane();
  }

  /**
   * Callback method that invokes when communication is closed.
   */
  @Override
  public void onCommunicationChannelClosed() {
    Logger.info("Communication closed, closing the GUI");
    Platform.runLater(Platform::exit);
  }

  /**
   * Callback method that invokes when a actuator is updated.
   *
   * @param nodeId   ID of the node on which this actuator is placed.
   * @param actuator The actuator that has changed its state.
   */
  @Override
  public void actuatorUpdated(int nodeId, Actuator actuator) {
    channel.sendActuatorChange(nodeId, actuator.getId(), actuator.isOn());
  }
}
