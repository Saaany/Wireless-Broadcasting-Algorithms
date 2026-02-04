package isb.util;

import java.io.*;
import java.nio.file.*;
import java.time.LocalDateTime;

public class MetadataWriter {

    public static void write(
            String filePath,
            int runs,
            double area,
            int[] nodes,
            double[] ranges) throws IOException {

        Path path = Paths.get(filePath);
        Files.createDirectories(path.getParent());

        try (BufferedWriter w = Files.newBufferedWriter(path)) {
            w.write("Improved Self-Pruning Broadcasting Experiments\n");
            w.write("Generated on: " + LocalDateTime.now() + "\n\n");

            w.write("Java Version: " + System.getProperty("java.version") + "\n");
            w.write("Runs per configuration: " + runs + "\n");
            w.write("Deployment area: " + area + " x " + area + " m\n\n");

            w.write("Node counts:\n");
            for (int n : nodes) w.write("  - " + n + "\n");

            w.write("\nTransmission ranges (m):\n");
            for (double r : ranges) w.write("  - " + r + "\n");
        }
    }
}
