package model;

import model.Player;
import static org.junit.jupiter.api.Assertions.*;

import org.json.JSONObject;
import org.junit.jupiter.api.*;

//Testes for each class, gaps identified.
class PlayerTest {

    Player player, player2, player3;

    @BeforeEach
    void runBefore() {
        player = new Player(5,5);
        player2 = new Player(5,8);
        player3 = new Player(9,2);
    }

    @Test
    void testPlayerXAndY() {
        assertEquals(5,player.getX());
        player.moveY(3);
        assertEquals(5, player.getX());
        assertEquals(8, player.getY());
        player.moveX(-2);
        assertEquals(8, player.getY());
        assertEquals(3, player.getX());
    }

    @Test
    void testToJSON() {
        JSONObject json = player.toJson();
        JSONObject json2 = player2.toJson();
        assertEquals(5, json.get("posX"));
        assertEquals(5, json2.get("posX"));
        assertEquals(8, json2.get("posY"));
    }

}