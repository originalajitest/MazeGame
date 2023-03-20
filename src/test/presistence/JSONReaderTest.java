package presistence;

import model.Maze;
import org.junit.jupiter.api.*;
import persistence.JsonReader;
import persistence.JsonWriter;

import java.io.IOException;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

public class JSONReaderTest {

    @Test
    void testReaderNonExistentFile() {
        JsonReader reader = new JsonReader("./data/noSuchFile.json");
        try {
            Map<String, Object> data = reader.read();
            fail("IOException expected");
        } catch (IOException e) {
            // pass
        }
    }

    @Test
    void testBasicSaveNoChanges() {
        JsonReader reader = new JsonReader("./data/testBasicSave.json");
        ArrayList<Integer> tempArrange = new ArrayList<>();
        tempArrange.add(0);
        tempArrange.add(1);
        tempArrange.add(2);
        tempArrange.add(3);
        tempArrange.add(4);
        tempArrange.add(5);
        try {
            Map<String, Object> data = reader.read();
            assertEquals(6, ((ArrayList<Integer>) data.get("arrangement")).size());
            assertEquals(tempArrange, (ArrayList<Integer>) data.get("arrangement"));
        } catch (IOException e) {
            fail("Couldn't read from file");
        }
        try {
            Map<String, Object> data = reader.read();
            LinkedList<Maze> mazes = (LinkedList<Maze>) data.get("mazes");
            assertEquals(6, mazes.size());
            String temp = "0 S 0 \n0   0 \n0   E \n";
            assertEquals(temp, mazes.get(0).printNormal());
            assertFalse(mazes.get(0).isSolved());
            assertFalse(mazes.get(0).isNotMaze());
        } catch (IOException e) {
            fail("Couldn't read from file");
        }
    }

}