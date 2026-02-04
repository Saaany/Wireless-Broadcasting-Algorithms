package isb.algorithms;

import isb.model.Node;
import java.util.*;

public class TotalDominantPruning {

    // this time we are assuming that we have 3 hop information

    public static Set<Integer> computeForwardList(
            Node u, Node prev, Map<Integer, Node> net) {

        Set<Integer> U = new HashSet<>();
        for (int vId : u.getNeighbors()) {
            Node v = net.get(vId);
            if (v == null) continue;

            U.addAll(v.getNeighbors()); // N^2(u)
        }
        // N^2(u) - N^2(prev)
        if (prev != null) {
            for (int wId : prev.getNeighbors()) {
                Node w = net.get(wId);
                if (w == null) continue;

                U.removeAll(w.getNeighbors());
            }
        }else{
            U.removeAll(u.getNeighbors()); // N^2(u) - N(u)
        }

        Set<Integer> B = new HashSet<>(u.getNeighbors());
        if(prev!= null) B.removeAll(prev.getNeighbors());
        B.remove(u.id);

        Set<Integer> F = new HashSet<>();

        while (!U.isEmpty() && !B.isEmpty()) {
            int best = -1;
            int maxCover = -1;

            for (int w : B) {
                Set<Integer> cover =
                        new HashSet<>(net.get(w).getNeighbors());
                cover.retainAll(U);

                if (cover.size() > maxCover) {
                    maxCover = cover.size();
                    best = w;
                }
            }

            if (maxCover <= 0) break;

            F.add(best);
            U.removeAll(net.get(best).getNeighbors());
            B.remove(best);
        }

        if(!U.isEmpty()){
            System.out.println("Improved Dominant Pruning could not cover all nodes in U");
        }

        return F;
    }
}
