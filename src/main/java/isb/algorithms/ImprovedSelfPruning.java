package isb.algorithms;

import isb.model.Node;
import java.util.*;

public class ImprovedSelfPruning {

    public static boolean shouldForward(
            Node v, Node u, Map<Integer, Node> net) {

        Set<Integer> U = new HashSet<>(v.getNeighbors());
        U.removeAll(u.getNeighbors());

        for (int xId : u.getNeighbors()) {
            Node x = net.get(xId);
            if (x == null || xId == u.id || xId == v.id) continue;

            if (x.degree() > v.degree() ||
                    (x.degree() == v.degree() && x.id < v.id)) {

                U.removeAll(x.getNeighbors());
                if (U.isEmpty()) {
                    return false; // fully covered
                }
            }
        }
        return !U.isEmpty();
    }
}
