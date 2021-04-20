package appli;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class PiocheTest {

    @Test
    public void testDrawCard(){

        Pioche pioche = new Pioche();

        assertDoesNotThrow(pioche::drawCard);

    }

    @Test
    public void testIsEmpty(){

        Pioche pioche = new Pioche();

        assertFalse(pioche.isEmpty());

        for (int i = 2; i <= 59; i++)
            assertDoesNotThrow(pioche::drawCard);

        assertTrue(pioche.isEmpty());

    }

}