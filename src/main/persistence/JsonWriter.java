package persistence;

import model.Mazes;
import org.json.JSONObject;
import com.fasterxml.jackson.databind.ObjectMapper;


import java.io.File;
import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.nio.file.Path;
import java.nio.file.Paths;

public class JsonWriter {
    private static final int TAB = 4;
    private PrintWriter writer;
    private final String destination;

    // EFFECTS: constructs writer to write to destination file
    public JsonWriter(String destination)
    {
        this.destination = destination;
    }

    // MODIFIES: this
    // EFFECTS: opens writer; throws FileNotFoundException if destination file cannot be opened for writing
    public void open() throws FileNotFoundException {
        writer = new PrintWriter(new File(destination));
    }

    // MODIFIES: this
    // EFFECTS: writes JSON representation of mazes to file
    public void write(Mazes mazes) {
        JSONObject json = mazes.toJson();
        saveToFile(json.toString(TAB));
    }

    public void write(Mazes mazes, long time) {
        JSONObject json = mazes.toJson();
        json.put("time", time);
        saveToFile(json.toString(TAB));
    }

    // MODIFIES: this
    // EFFECTS: closes writer
    public void close() {
        writer.close();
    }

    // MODIFIES: this
    // EFFECTS: writes string to file
    private void saveToFile(String json) {
//        writer.print(json);

        try {
            // Get the root URI of the resources
            Path resourceRootPath = Paths.get(JsonWriter.class.getClassLoader().getResource("").toURI());

            // Create a File object for the destination location
            File destinationFile = new File(resourceRootPath.toFile(), destination);

            // Use Jackson ObjectMapper to write JSON to file
            ObjectMapper objectMapper = new ObjectMapper();
            objectMapper.writeValue(destinationFile, objectMapper.readTree(json));
            System.out.println("JSON written to: " + destinationFile.getAbsolutePath());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}