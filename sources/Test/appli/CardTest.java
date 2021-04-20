package appli;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class CardTest {

    @Test
    public void testConstructor(){

        assertThrows(IllegalArgumentException.class, () -> new Card(0));
        assertThrows(IllegalArgumentException.class, () -> new Card(61));
        assertThrows(IllegalArgumentException.class, () -> new Card(-60));

        assertDoesNotThrow(() -> new Card(5));
        assertDoesNotThrow(() -> new Card(1));
        assertDoesNotThrow(() -> new Card(60));

    }

    @Test
    @DisplayName("Constructeur de Carte par copie")
    public void testConstructorCopy(){

        Card card = new Card(5);
        Card copy = new Card(card);

        assertFalse(card == copy);
        assertEquals(card, copy);

    }

    @Test
    public void testToString(){

        Card c = new Card(5);
        assertEquals("05", c.toString());

        c = new Card(50);
        assertEquals("50", c.toString());

    }

    @Test
    public void testEquals(){

        Card c1 = new Card(5);
        Card c2 = new Card(5);
        Card c3 = new Card(3);

        assertEquals(c1, c1);
        assertNotEquals(null, c1);
        assertNotEquals("05", c1);

        assertEquals(c1, c2);
        assertEquals(c2, c1);

        assertNotEquals(c1, c3);
        assertNotEquals(c3, c1);

    }

}