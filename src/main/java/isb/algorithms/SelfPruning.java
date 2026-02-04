package isb.algorithms;

import isb.model.Node;
import java.util.*;

public class SelfPruning {
    public static boolean shouldForward(Node v, Node u) {
        Set<Integer> diff = new HashSet<>(v.getNeighbors());
        diff.removeAll(u.getNeighbors());
        return !diff.isEmpty();
    }
}
