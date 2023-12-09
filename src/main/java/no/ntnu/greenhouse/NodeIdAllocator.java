package no.ntnu.greenhouse;

import java.util.HashSet;
import java.util.Set;

/**
 * Class for allocating node IDs.
 * This class is thread-safe.
 */
public class NodeIdAllocator {
  private final Set<Integer> allocatedNodeIds = new HashSet<>();
  private int nextNodeId = 1;

  /**
   * Assign a new node ID.
   *
   * @return A new node ID.
   */
  public synchronized int assignNodeId() {
    while (allocatedNodeIds.contains(nextNodeId)) {
      nextNodeId++;
    }
    allocatedNodeIds.add(nextNodeId);
    return nextNodeId;
  }

  public synchronized void unassignNodeId(int nodeId) {
    allocatedNodeIds.remove(nodeId);
  }
}