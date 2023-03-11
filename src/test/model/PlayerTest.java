package model;

import model.Player;
import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.*;

//Testes for each class, gaps identified.
class PlayerTest {

    Player player;

    @BeforeEach
    void runBefore() {
        player = new Player(5,5);
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

}