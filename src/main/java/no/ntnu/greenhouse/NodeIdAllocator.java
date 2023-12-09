package no.ntnu.greenhouse;

import java.util.HashSet;
import java.util.Set;

public class NodeIdAllocator {
    private final Set<Integer> allocatedNodeIds = new HashSet<>();
    private int nextNodeId = 1;

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