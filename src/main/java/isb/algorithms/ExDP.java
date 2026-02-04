package isb.algorithms;

import isb.model.Node;

import java.util.HashSet;
import java.util.Map;
import java.util.Set;

public class ExDP {

    public static Set<Integer> computeForwardList(
            Node v,
            Node prev,
            Map<Integer, Node> net,
            Set<Integer> Fu              // F(u)
    ) {

        // ---------- Step 1: Qv ----------
        Set<Integer> Qv = new HashSet<>();

        for (int fid : Fu) {
            if (fid == v.id) continue;

            Node fi = net.get(fid);

            if (fi != null &&
                    fi.id < v.id &&
                    v.getNeighbors().contains(fi.id)) {

                // add N(N(fi))
                for (int x : fi.getNeighbors()) {
                    Node n2 = net.get(x);
                    if (n2 != null) {
                        Qv.addAll(n2.getNeighbors());
                    }
                }
            }
        }

        // ---------- Step 2: Uv ----------
        Set<Integer> NNv = twoHop(v, net);
        Set<Integer> NNu;
        if (prev != null) {
            NNu = twoHop(prev, net);
        }else {
            NNu = new HashSet<>(v.getNeighbors());
        }

        Set<Integer> Uv = new HashSet<>(NNv);
        Uv.removeAll(NNu);
        Uv.removeAll(Qv);

        // ---------- Step 3: Bv ----------
        Set<Integer> Bv = new HashSet<>(v.getNeighbors());
        if(prev!=null) Bv.removeAll(prev.getNeighbors());

        // ---------- Step 4: Greedy set cover (same as DP) ----------
        return greedySetCover(Uv, Bv, net);
    }

    // -------- helper: 2-hop neighbors --------
    private static Set<Integer> twoHop(Node v, Map<Integer, Node> net) {
        Set<Integer> res = new HashSet<>();
        for (int n1 : v.getNeighbors()) {
            Node x = net.get(n1);
            if (x != null) {
                res.addAll(x.getNeighbors());
            }
        }
        return res;
    }

    // -------- helper: DP selection --------
    private static Set<Integer> greedySetCover(
            Set<Integer> U,
            Set<Integer> B,
            Map<Integer, Node> net
    ) {
        Set<Integer> F = new HashSet<>();

        Set<Integer> uncovered = new HashSet<>(U);

        while (!uncovered.isEmpty()) {

            int best = -1;
            int max = -1;

            for (int b : B) {
                Node z = net.get(b);
                if (z == null) continue;

                Set<Integer> cover = new HashSet<>(z.getNeighbors());
                cover.retainAll(uncovered);

                if (cover.size() > max) {
                    max = cover.size();
                    best = b;
                }
            }

            if (best == -1) break;

            F.add(best);
            uncovered.removeAll(net.get(best).getNeighbors());
            B.remove(best);
        }

        return F;
    }
}

