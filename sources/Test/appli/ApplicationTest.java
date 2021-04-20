package appli;

import appli.types.ActionType;
import appli.types.PlayerType;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class ApplicationTest {

    /*
     * Cette classe permet de simuler différents scénarios particuliers,
     * difficiles a traiter.
     */
    @Test
    public void testScenarios(){

        /*
         *  NORD ^[24] v[46] (m6p45)
         *  SUD ^[25] v[52] (m6p46)
         *  cartes NORD { 10 13 30 52 57 58 }
         *
         *  Action : 58v' 30^
         *
         *  Le bug est sûrement lié à un problème de caractère lorsqu'il a été tapé
         */
        Player p = new Player(PlayerType.NORD);
        Player e = new Player(PlayerType.SUD);

        p.getPileAsc().add(new Card(24));
        p.getPileDesc().add(new Card(46));
        e.getPileAsc().add(new Card(25));
        e.getPileDesc().add(new Card(52));

        int[] val = { 10, 13, 30, 52, 57, 58 };

        for(int v : val)
            p.addCardInHand(new Card(v));

        ActionBuffer buffer = new ActionBuffer(p,e);
        Action a1 = new Action(ActionType.ADD_DESC_ENEMY, new Card(58));
        Action a2 = new Action(ActionType.ADD_ASC, new Card(30));

        assertTrue(buffer.canAdd(a1));
        assertDoesNotThrow(() -> buffer.add(a1));

        assertTrue(buffer.canAdd(a2));
        assertDoesNotThrow(() -> buffer.add(a2));

        assertDoesNotThrow(() -> p.executeActionBuffer(buffer));

        assertEquals(new Card(30), p.getPileAsc().getTop());
        assertEquals(new Card(58), e.getPileDesc().getTop());

    }

}