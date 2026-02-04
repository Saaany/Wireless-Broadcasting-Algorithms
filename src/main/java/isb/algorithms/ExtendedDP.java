package isb.algorithms;

import isb.model.Node;
import java.util.*;

import static java.lang.System.exit;

public class ExtendedDP {

    // this time we are assuming that we have 3 hop information

    public static Set<Integer> computeForwardList(
            Node u, Node prev, Map<Integer, Node> net, Set<Integer> forwardListOfPrev) {

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

        // N^2(u) - N^2(prev) - Union of N^2(w) where w belongs to FL(prev) && N(u) and w.id < u.id
        for (int wId : forwardListOfPrev) {

//            if (!net.get(wId).hasReceived()) {
//                System.out.println("LIE IN Qv: " + wId + " never received packet");
////                exit(0);
//            }

            boolean flag = false;

            if (u.getNeighbors().contains(wId) && wId < u.id) {
                Node w = net.get(wId);
                if (w == null) continue;

                for (int zId : w.getNeighbors()) {
                    Node z = net.get(zId);
                    if (z == null) continue;

                    U.removeAll(z.getNeighbors());

                    if(U.isEmpty()){
                        flag = true;
                        break;
                    }
                }
            }
            if(flag) break;
        }

//        System.out.println("U after 3-hop reduction: " + U);

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

//        if(!U.isEmpty()){
//            System.out.println("Extended Dominant Pruning could not cover all nodes in U");
//        }
        System.out.println("prev: " + (prev != null ? prev.id : "null") + " u: " + u.id + " U: " + U + " F: " + F);
        return F;
    }
}
