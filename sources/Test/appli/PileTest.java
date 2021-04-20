package appli;

import appli.types.PileType;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class PileTest {

    @Test
    public void testCanAdd(){

        Pile pileAsc = new Pile(PileType.ASC);
        assertTrue(pileAsc.canAdd(new Card(2)));
        assertTrue(pileAsc.canAdd(new Card(40)));
        pileAsc.add(new Card(40));
        assertFalse(pileAsc.canAdd(new Card(35)));
        assertTrue(pileAsc.canAdd(new Card(41)));

        Pile pileDesc = new Pile(PileType.DESC);
        assertTrue(pileDesc.canAdd(new Card(58)));
        pileDesc.add(new Card(58));
        assertTrue(pileDesc.canAdd(new Card(53)));
        assertFalse(pileDesc.canAdd(new Card(60)));

        /*
         * On vérifie la règle de +10 & -10
         */
        pileAsc = new Pile(PileType.ASC);
        pileDesc = new Pile(PileType.DESC);

        // +10
        pileDesc.add(new Card(48));
        assertFalse(pileDesc.canAdd(new Card(55)));
        assertTrue(pileDesc.canAdd(new Card(58)));
    }

    @Test
    public void testAdd(){

        Pile pileAsc = new Pile(PileType.ASC);
        assertTrue(pileAsc.canAdd(new Card(2)));
        assertTrue(pileAsc.canAdd(new Card(40)));

        Pile pileDesc = new Pile(PileType.DESC);
        assertTrue(pileDesc.canAdd(new Card(55)));
        assertFalse(pileDesc.canAdd(new Card(60)));

    }

}