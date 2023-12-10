package no.ntnu.greenhouse;

/**
 * Interface for nodes.
 */
public interface NodeInterface {
  int getId();

  ActuatorCollection getActuators();
}
