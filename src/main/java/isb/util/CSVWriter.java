package isb.util;

import java.io.*;
import java.nio.file.*;

public class CSVWriter {

    private final BufferedWriter writer;

    public CSVWriter(String filePath, String header) throws IOException {
        Path path = Paths.get(filePath);
        Files.createDirectories(path.getParent());

        writer = Files.newBufferedWriter(path);
        writer.write(header);
        writer.newLine();
    }

    public void writeRow(String row) throws IOException {
        writer.write(row);
        writer.newLine();
    }

    public void close() throws IOException {
        writer.flush();
        writer.close();
    }
}
