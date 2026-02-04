package isb.simulation;

import isb.model.Node;
import isb.network.NetworkGenerator;
import isb.util.CSVWriter;
import isb.util.MetadataWriter;

import java.util.*;

public class UpdatedExperimentRunner {

    public static void main(String[] args) throws Exception {

        int[] nodeCounts = {100, 200, 350, 400, 500};
        double[] ranges = {125, 150, 175, 200, 225};
        double area = 625.0;
        int runs = 10;

        Random rand = new Random(42);

        // ---- Save metadata ----
        MetadataWriter.write(
                "results/metadata.txt",
                runs, area, nodeCounts, ranges
        );

        // ---- CSV output ----
        CSVWriter csv = new CSVWriter(
                "results/forwarding_nodes.csv",
                "Nodes,Range,SP,DP,ISP,ESP,EESP,IDP"
        );

        for (int n : nodeCounts) {
            for (double r : ranges) {

                double sp = 0, dp = 0, isb = 0, esp = 0, eesp = 0, idp = 0;

                for (int i = 0; i < runs; i++) {
                    Map<Integer, Node> net =
                            NetworkGenerator.generate(n, area, r, rand);

                    int source = 1 + rand.nextInt(n);

                    sp  += BroadcastEngine.run(source, net, "SP",i);
                    dp  += BroadcastEngine.run(source, net, "DP",i);
                    isb += BroadcastEngine.run(source, net, "ISP",i);
                    esp += BroadcastEngine.run(source, net, "ESP",i);
                    eesp += BroadcastEngine.run(source, net, "EESP",i);
//                    idp += BroadcastEngine.run(source, net, "IDP",i);
                }

                csv.writeRow(String.format(
                        "%d,%.0f,%.2f,%.2f,%.2f,%.2f,%.2f,%.2f",
                        n, r,
                        sp / runs,
                        dp / runs,
                        isb / runs,
                        esp / runs,
                        eesp / runs,
                        idp / runs
                ));
            }
        }

        csv.close();
        System.out.println("✅ Results (SP, DP, ISP, ESP, EESP, IDP) saved in /results/");
    }
}
