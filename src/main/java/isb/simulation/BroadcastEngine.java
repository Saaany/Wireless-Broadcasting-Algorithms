package isb.simulation;

import isb.model.*;
import isb.algorithms.*;
import java.util.*;

public class BroadcastEngine {

    public static int run(
            int source,
            Map<Integer, Node> net,
            String mode,
            int runId) {

        for (Node n : net.values()) n.reset();

        Queue<Packet> queue = new LinkedList<>();
        int forwards = 0;
        int fl_total = 1;
        int DP_forwards = 0;

        Node src = net.get(source);
        src.markReceived();
        forwards++;
        DP_forwards++;

        // Source computes DP forwarding list once
        Set<Integer> srcFL = null;
        switch (mode) {
            case "DP":
                srcFL = DominantPruning.computeForwardList(src, null, net);
                break;
            case "TDP":
                srcFL = TotalDominantPruning.computeForwardList(src, null, net);
                break;
            case "PDP":
                srcFL = PartialDominantPruning.computeForwardList(src, null, net);
                break;
            case "ExDP":
                srcFL = ExtendedDP.computeForwardList(src, null, net, new HashSet<>());
//                srcFL = ExDP.computeForwardList(src, null, net, new HashSet<>());
                break;
            case "IDP":
                srcFL = ImprovedDominantPruning.computeForwardList(src, null, net, null, new HashSet<>());
                break;
        }

        queue.add(new Packet(source, source, srcFL));

        fl_total += (srcFL != null) ? srcFL.size() : 0;

        boolean DP_MODE = mode.equals("DP") || mode.equals("PDP") || mode.equals("TDP") || mode.equals("ExDP") || mode.equals("IDP");

        while (!queue.isEmpty()) {

            Packet pkt = queue.poll();
            Node u = net.get(pkt.senderId);
//            u.markReceived();
            u.markForwarded();

            boolean forwardsPacket;

            for (int vId : u.getNeighbors()) {
                Node v = net.get(vId);

                forwardsPacket = false;

                if(DP_MODE) {
                    // Dominant Pruning variants rely on forwardList to determine forwarding
                    if (pkt.forwardList.size() == 0 || !pkt.forwardList.contains(v.id) || v.hasForwarded()) {
                        v.markReceived();
                        continue; // v is not in the forward list, skip
                    }
                }
                else if (v == null || v.hasReceived()) continue;

                v.markReceived();

                switch (mode) {
                    case "SP":
                        forwardsPacket = SelfPruning.shouldForward(v, u);
                        break;
                    case "ISP":
                        forwardsPacket = ImprovedSelfPruning.shouldForward(v, u, net);
                        break;
                    case "DP":
                    case "PDP":
                    case "TDP":
                    case "ExDP":
                    case "IDP":
                        forwardsPacket = true;
                        v.markForwarded();
                        break;
                    case "ESP":
                        forwardsPacket = EnhancedSelfPruning.shouldForward(v, u, net, net.get(pkt.sourceId));
//                        forwardsPacket = EnhancedSelfPruning.shouldForward(v, u, net, u);
                        break;
                    case "EESP":
                        forwardsPacket = EESelfPruning.shouldForward(v, u, net, net.get(pkt.sourceId));
//                        forwardsPacket = EESelfPruning.shouldForward(v, u, net, u);
                        break;
                    default:
                        throw new IllegalArgumentException("Unknown mode: " + mode);
                }

                if (forwardsPacket) {
                    forwards++;
                    DP_forwards++;

                    Set<Integer> forwardList = null;
                    switch (mode) {
                        case "DP":
                            forwardList = DominantPruning.computeForwardList(v, u, net);
                            fl_total += forwardList.size();
                            break;
                        case "PDP":
                            forwardList = PartialDominantPruning.computeForwardList(v, u, net);
                            fl_total += forwardList.size();
                            break;
                        case "TDP":
                            forwardList = TotalDominantPruning.computeForwardList(v, u, net);
                            fl_total += forwardList.size();
                            break;
                        case "ExDP":
                            forwardList = ExtendedDP.computeForwardList(v, u, net, pkt.forwardList);
//                            forwardList = ExDP.computeForwardList(v, u, net, pkt.forwardList);
//                            System.out.println("FL at node " + v.id + ": " + forwardList);
                            fl_total += forwardList.size();
                            break;
                        case "IDP":
                            forwardList = ImprovedDominantPruning.computeForwardList(v, u, net, net.get(pkt.sourceId), pkt.forwardList);
//                            forwardList = ImprovedDominantPruning.computeForwardList(v, u, net, u, pkt.forwardList);
                            fl_total += forwardList.size();
                            break;
                    }

                    queue.add(new Packet(u.id, v.id, forwardList));
                }
            }
        }

        checkAllReceived(net,mode,runId,net.get(source));
        return DP_MODE ? DP_forwards : forwards;
    }

    private static void checkAllReceived(Map<Integer, Node> net, String mode,int runID,Node src) {
        int notReceived = 0;
        for (Node n : net.values()) {
            if (!n.hasReceived()) {
                notReceived++;
                System.out.println("Run - "+(runID+1)+": Src("+src.toString()+"): Node " + n.toString() +" did not receive the packet in mode " + mode);
//                System.out.println("Neighbors: " + n.getNeighbors().toString());
//                if(notReceived > 8) exit(0);
            }
        }
    }
}
