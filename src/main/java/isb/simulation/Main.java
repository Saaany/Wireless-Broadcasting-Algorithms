package isb.simulation;

import isb.algorithms.ExtendedDP;
import isb.model.Node;
import isb.network.NetworkGenerator;

import java.util.HashMap;
import java.util.Map;

public class Main {
    public static void main(String[] args) {

        Map<Integer, Node> network = new HashMap<>();

        for(int i = 1;i<=12;i++){
            network.put(i, new Node(i,0,0));
            network.get(i).addNeighbor(i);
        }

        network.get(1).addNeighbor(2);
        network.get(2).addNeighbor(1);

        network.get(1).addNeighbor(7);
        network.get(7).addNeighbor(1);

        network.get(1).addNeighbor(8);
        network.get(8).addNeighbor(1);

        network.get(2).addNeighbor(3);
        network.get(3).addNeighbor(2);

        network.get(2).addNeighbor(8);
        network.get(8).addNeighbor(2);

        network.get(2).addNeighbor(10);
        network.get(10).addNeighbor(2);

        network.get(3).addNeighbor(4);
        network.get(4).addNeighbor(3);

        network.get(3).addNeighbor(11);
        network.get(11).addNeighbor(3);

        network.get(4).addNeighbor(5);
        network.get(5).addNeighbor(4);

        network.get(4).addNeighbor(11);
        network.get(11).addNeighbor(4);

        network.get(5).addNeighbor(6);
        network.get(6).addNeighbor(5);

        network.get(6).addNeighbor(12);
        network.get(12).addNeighbor(6);

        network.get(7).addNeighbor(8);
        network.get(8).addNeighbor(7);

        network.get(8).addNeighbor(9);
        network.get(9).addNeighbor(8);

        network.get(9).addNeighbor(10);
        network.get(10).addNeighbor(9);

        network.get(10).addNeighbor(11);
        network.get(11).addNeighbor(10);

        network.get(11).addNeighbor(12);
        network.get(12).addNeighbor(11);


        int src = 2;
        int exdp = BroadcastEngine.run(src, network, "ExDP", 0);

        System.out.println("Extended Dominant Pruning selected " + exdp + " forwarding nodes.");
        System.out.println("------------------------------------------------------");

        // The network is now set up for simulation of Extended Dominant Pruning
        int idp = BroadcastEngine.run(src, network, "IDP", 0);
        System.out.println("Improved Dominant Pruning selected " + idp + " forwarding nodes.");



    }
}
