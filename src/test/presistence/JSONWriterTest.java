package presistence;

import model.Maze;
import model.Mazes;
import org.junit.jupiter.api.*;
import persistence.JsonReader;
import persistence.JsonWriter;

import java.io.IOException;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

class JsonWriterTest {

    Mazes mazes;
    ArrayList<Integer> arrange;

    @BeforeEach
    void beforeEach() {
        arrange = new ArrayList<>();
        arrange.add(0);
        mazes = new Mazes(arrange);
    }

    @Test
    void testWriterInvalidFile() {
        try {
            JsonWriter writer = new JsonWriter("./data/my\0illegal:fileName.json");
            writer.open();
            fail("IOException was expected");
        } catch (IOException e) {
            // pass
        }
    }

    @Test
    void test1Maze() {
        try {
            Mazes mazes = new Mazes(arrange);
            JsonWriter writer = new JsonWriter("./data/test1Maze.json");
            writer.open();
            writer.write(mazes);
            writer.close();

            JsonReader reader = new JsonReader("./data/test1Maze.json");
            Mazes importedMazes = new Mazes(reader.read());
            assertEquals(arrange, importedMazes.getArrangement());
            assertFalse(!mazes.checkAllSolved());
        } catch (IOException e) {
            fail("Exception should not have been thrown");
        }
    }

    @Test
    void test2Maze() {
        try {
            arrange.add(3);
            Mazes mazes = new Mazes(arrange);
            JsonWriter writer = new JsonWriter("./data/test2Maze.json");
            writer.open();
            writer.write(mazes);
            writer.close();

            JsonReader reader = new JsonReader("./data/test2Maze.json");
            Map<String, Object> data = reader.read();
            assertEquals(arrange.size(), ((ArrayList<Integer>) data.get("arrangement")).size());
            LinkedList<Maze> importedMazes = (LinkedList<Maze>) data.get("mazes");
            assertEquals(arrange.size(), importedMazes.size());
        } catch (IOException e) {
            fail("Exception should not have been thrown");
        }
    }

}