package isb.algorithms;

import isb.model.Node;

import java.util.HashSet;
import java.util.Map;
import java.util.Set;

public class EESelfPruning {

    public static boolean shouldForward(
            Node u, Node parent, Map<Integer, Node> net, Node grandparent) {

        int gid=-1;
        Set<Integer> U = new HashSet<>(u.getNeighbors());
        U.removeAll(parent.getNeighbors());
        if (grandparent != null) {
            U.removeAll(grandparent.getNeighbors());
            gid = grandparent.id;
        }

        if(U.isEmpty()) return false;

        // --- Step 2: compute Q once ---
        Set<Integer> Nu = u.getNeighbors();
        Set<Integer> Np = parent.getNeighbors();
        Set<Integer> Ng = (grandparent != null) ? grandparent.getNeighbors() : new HashSet<>();
        Set<Integer> Npg = union(Np, Ng);

        int Qc = differenceSize(Nu, intersection(Nu, Npg));
        int Qp = differenceSize(Nu, intersection(Nu, Np));
        int Qg = differenceSize(Nu, intersection(Nu, Ng));

        // --- Step 3: compute S ---
        for (int zid : Npg) {
            if(zid == u.id || zid == parent.id || zid == gid) continue;
            Node z = net.get(zid);
            if (z == null) continue;

            int P = differenceSize(z.getNeighbors(), intersection(z.getNeighbors(), Np));
            if (P > Qp || (P==Qp && z.id < u.id)) {
                U.removeAll(z.getNeighbors());
            }

            if (U.isEmpty()) {
                return false; // fully covered
            }
        }

        return !U.isEmpty();
    }

    // ---------- Helper methods ----------

    private static Set<Integer> intersection(Set<Integer> a, Set<Integer> b) {
        Set<Integer> r = new HashSet<>(a);
        r.retainAll(b);
        return r;
    }

    private static Set<Integer> union(Set<Integer> a, Set<Integer> b) {
        Set<Integer> r = new HashSet<>(a);
        r.addAll(b);
        return r;
    }

    private static int differenceSize(Set<Integer> a, Set<Integer> b) {
        Set<Integer> r = new HashSet<>(a);
        r.removeAll(b);
        return r.size();
    }

    private static double distance(Node a, Node b) {
        double dx = a.x - b.x;
        double dy = a.y - b.y;
        return Math.sqrt(dx * dx + dy * dy);
    }

}

