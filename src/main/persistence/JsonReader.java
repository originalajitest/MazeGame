package persistence;

import com.fasterxml.jackson.databind.ObjectMapper;
import model.Maze;
import org.json.JSONArray;
import org.json.JSONObject;

import java.io.File;
import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.Map;

public class JsonReader {
    private final String source;

    // EFFECTS: constructs reader to read from source file
    public JsonReader(String source) {
        this.source = source;
    }

    // EFFECTS: reads Mazes from file and returns it; throws IOException if an error occurs reading data from file
    public Map<String, Object> read() throws IOException {
        String jsonData = readFile(source);
        JSONObject jsonObject = new JSONObject(jsonData);
        return parseMazes(jsonObject);
    }

    // EFFECTS: reads source file as string and returns it
    private String readFile(String source) throws IOException{

        try {
            // Get the root URI of the resources
            Path resourceRootPath = Paths.get(JsonReader.class.getClassLoader().getResource("").toURI());
//            System.out.println(JsonReader.class.getResource("").toString());
//            System.out.println(JsonReader.class.getClassLoader().getParent().getClass().getClassLoader().getResource("").toString());
//            Path resourceRootPath = Paths.get(JsonReader.class.getResource("").toURI());
//            System.out.println(resourceRootPath);
//            System.out.println("");

            // Create a File object for the source location
            File sourceFile = new File(resourceRootPath.toFile(), source);

            // Use Jackson ObjectMapper to read JSON from file
            ObjectMapper objectMapper = new ObjectMapper();
            return objectMapper.readTree(sourceFile).toString();
        } catch (Exception e) {
            e.printStackTrace();
            throw new IOException();
        }
    }

    // EFFECTS: parses Mazes from JSON object and returns it
    private Map<String, Object> parseMazes(JSONObject jsonObject) {
        Map<String, Object> ans = new HashMap<>();
        JSONArray arrangeObj = jsonObject.getJSONArray("arrangement");
        ArrayList<Integer> arrangement = new ArrayList<>();
        for (Object i: arrangeObj) {
            arrangement.add((Integer) i);
        }
        ans.put("arrangement", arrangement);

        JSONArray mazesObj = jsonObject.getJSONArray("mazes");
        LinkedList<Maze> mazes = new LinkedList<>();
        for (Object i: mazesObj) {
            mazes.add(toMaze((JSONObject) i));
        }
        ans.put("mazes", mazes);
        ans.put("color", jsonObject.get("color"));
        ans.put("time", jsonObject.get("time"));
        return ans;
    }

    //EFFECTS: returns a Maze object which has attributes of given data.
    private Maze toMaze(JSONObject json) {
        return new Maze(json.toMap());
    }
}