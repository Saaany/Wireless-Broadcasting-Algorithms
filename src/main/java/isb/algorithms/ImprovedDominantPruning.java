package isb.algorithms;

import isb.model.Node;
import java.util.*;

public class ImprovedDominantPruning {

    // this time we are assuming that we have 3 hop information

    public static Set<Integer> computeForwardList(
            Node u, Node prev, Map<Integer, Node> net, Node grandparent, Set<Integer> forwardListOfPrev) {

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

        // N^2(u) - N^2(prev) - N^2(grandparent)
        if (grandparent != null) {
            for (int xId : grandparent.getNeighbors()) {
                Node x = net.get(xId);
                if (x == null) continue;

                U.removeAll(x.getNeighbors());
            }
        }

        // N^2(u) - N^2(prev) - N^2(grandparent) - Union of N^2(w) where w belongs to forward list of prev and w.id < u.id
        if (prev != null) {

            for (int wId : forwardListOfPrev) {
                if (wId < u.id) {
                    Node w = net.get(wId);
                    if (w == null) continue;

                    for (int zId : w.getNeighbors()) {
                        Node z = net.get(zId);
                        if (z == null) continue;

                        U.removeAll(z.getNeighbors());
                    }
                }
            }
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
        System.out.println("prev: " + (prev != null ? prev.id : "null") + " u: " + u.id + " U: " + U + " F: " + F);
        return F;
    }
}
