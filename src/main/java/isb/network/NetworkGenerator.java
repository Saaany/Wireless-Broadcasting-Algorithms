package isb.network;

import isb.model.Node;
import java.util.*;

public class NetworkGenerator {

    public static Map<Integer, Node> generate(int n, double area, double range, Random r) {

        Map<Integer, Node> net = new HashMap<>();

        for (int i = 1; i <= n; i++) {
            net.put(i, new Node(
                    i,
                    r.nextDouble() * area,
                    r.nextDouble() * area));
        }

        for (Node u : net.values()) {
            for (Node v : net.values()) {
                if (distance(u, v) <= range) {
                    u.addNeighbor(v.id);
                }
            }
        }
        return net;
    }

    private static double distance(Node a, Node b) {
        double dx = a.x - b.x;
        double dy = a.y - b.y;
        return Math.sqrt(dx * dx + dy * dy);
    }
}
