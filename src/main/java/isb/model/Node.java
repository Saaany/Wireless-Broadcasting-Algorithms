package isb.model;

import java.util.*;

public class Node {
    public final int id;
    public final double x, y;

    private final Set<Integer> neighbors = new HashSet<>();
    private boolean received = false;
    private boolean forwarded = false;

    public Node(int id, double x, double y) {
        this.id = id;
        this.x = x;
        this.y = y;
        neighbors.add(id);
    }

    public void addNeighbor(int nid) {
        neighbors.add(nid);
    }

    public Set<Integer> getNeighbors() {
        return Collections.unmodifiableSet(neighbors);
    }

    public int degree() {
        return neighbors.size();
    }

    public boolean hasReceived() {
        return received;
    }

    public void markReceived() {
        received = true;
    }

    public boolean hasForwarded() {
        return forwarded;
    }
    public void markForwarded() {
        forwarded = true;
    }

    public void reset() {
        received = false;
        forwarded = false;
    }

    @Override
    public String toString() {
        return "Node{" +
                "id=" + id +
                ", received=" + received +
                "} Neighbors:" + neighbors;
    }
}
