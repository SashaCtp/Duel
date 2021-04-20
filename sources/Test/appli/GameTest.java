package appli;

import appli.types.PlayerType;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class GameTest {

    @Test
    public void testConstructor(){

        assertThrows(IllegalArgumentException.class, () -> new Game(new Player(), new Player(PlayerType.SUD)));
        assertThrows(IllegalArgumentException.class, () -> new Game(new Player(PlayerType.SUD), new Player(PlayerType.SUD)));
        assertThrows(IllegalArgumentException.class, () -> new Game(new Player(PlayerType.NORD), new Player(PlayerType.NORD)));

        assertDoesNotThrow(() -> new Game(new Player(PlayerType.NORD), new Player(PlayerType.SUD)));

    }

}