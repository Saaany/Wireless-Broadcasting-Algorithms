package isb.simulation;

import isb.model.Node;
import isb.network.NetworkGenerator;
import isb.util.CSVWriter;
import isb.util.MetadataWriter;

import java.util.*;

public class DPExperimentRunner {

    public static void main(String[] args) throws Exception {

        int[] nodeCounts = {100, 200, 300, 400, 500};
        double[] ranges = {125, 150, 175, 200, 225};
        double area = 650.0;
        int runs = 10;

        Random rand = new Random(42);

        // ---- Save metadata ----
        MetadataWriter.write(
                "results/DP_metadata.txt",
                runs, area, nodeCounts, ranges
        );

        // ---- CSV output ----
        CSVWriter csv = new CSVWriter(
                "results/DP_forwarding_nodes.csv",
                "Nodes,Range,DP,PDP,TDP,ExDP,IDP"
        );

        for (int n : nodeCounts) {
            for (double r : ranges) {

                double dp = 0, pdp = 0, tdp = 0, exdp = 0, idp = 0;

                for (int i = 0; i < runs; i++) {
                    Map<Integer, Node> net =
                            NetworkGenerator.generate(n, area, r, rand);

                    int source = 1 + rand.nextInt(n);
//                    System.out.println("Run " + (i+1) + "/" + runs + " started for N=" + n + ", R=" + r);
                    dp  += BroadcastEngine.run(source, net, "DP",i);
                    pdp  += BroadcastEngine.run(source, net, "PDP",i);
                    tdp += BroadcastEngine.run(source, net, "TDP",i);
                    exdp += BroadcastEngine.run(source, net, "ExDP",i);
                    idp += BroadcastEngine.run(source, net, "IDP",i);

//                    System.out.println("Run " + (i+1) + "/" + runs + " completed for N=" + n + ", R=" + r);
//                    System.out.println("====================================================\n\n");
                }

                csv.writeRow(String.format(
                        "%d,%.0f,%.2f,%.2f,%.2f,%.2f,%.2f",
                        n, r,
                        dp / runs,
                        pdp / runs,
                        tdp / runs,
                        exdp / runs,
                        idp / runs
                ));
            }
        }

        csv.close();
        System.out.println("✅ Results saved in /results/");
    }
}
