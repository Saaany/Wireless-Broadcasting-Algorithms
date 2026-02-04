package isb.algorithms;

import isb.model.Node;

import java.util.HashSet;
import java.util.Map;
import java.util.Set;

public class EnhancedSelfPruning {

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

        int Q = differenceSize(Nu, intersection(Nu, Np));
        // --- Step 3: compute S ---
        for (int zid : Np) {
            if(zid == u.id || zid == parent.id || zid == gid) continue;
            Node z = net.get(zid);
            if (z == null) continue;

            Set<Integer> Nz = z.getNeighbors();
            int P = differenceSize(Nz, intersection(Nz, Np));

            if (P > Q || (P==Q && z.id < u.id)) {

                U.removeAll(Nz);

                if (U.isEmpty()) {
                    return false; // fully covered
                }
            }
//            if(P>Q){
//                U.removeAll(Nz);
//            } else if (P == Q) {
//                double du = distance(u, parent);
//                double dz = distance(z, parent);
//                // tie-breaker based on distance to parent
//                if (dz > du || (dz == du && z.id < u.id)) {
//                    U.removeAll(Nz);
//                }
//            }

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
