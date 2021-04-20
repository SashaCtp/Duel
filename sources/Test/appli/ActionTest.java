package appli;

import appli.types.ActionType;
import appli.types.PileType;
import appli.types.PlayerType;
import org.junit.jupiter.api.Test;

import java.util.HashMap;

import static org.junit.jupiter.api.Assertions.*;

class ActionTest {

    @Test
    void constructorByString(){

        HashMap<String, Boolean> tests = new HashMap<>();
        tests.put("erreur de saisie", false);
        tests.put("1^^'", false);
        tests.put("09^^", false);
        tests.put("", false);
        tests.put("05^", true);
        tests.put("45v", true);
        tests.put("45^'", true);

        for(String key : tests.keySet()){

            if(tests.get(key))
                assertDoesNotThrow(() -> new Action(key));
            else
                assertThrows(Exception.class ,() -> new Action(key));

        }
    }

    @Test
    void isEnemyTarget(){

        Action action = new Action(ActionType.ADD_ASC_ENEMY, new Card(5));
        assertTrue(action.isEnemyTarget());

        action.setType(ActionType.ADD_DESC_ENEMY);
        assertTrue(action.isEnemyTarget());

        action.setType(ActionType.ADD_ASC);
        assertFalse(action.isEnemyTarget());

        action.setType(ActionType.ADD_DESC);
        assertFalse(action.isEnemyTarget());

    }

    @Test
    void isValid(){

        Player p;
        Player e;
        Action action;
        ActionBuffer actionBuffer;

        /*
         * On vérifie que les actions comportant des
         * types et cartes nulles sont non valides
         */
        p = new Player(PlayerType.SUD);
        e = new Player(PlayerType.NORD);

        p.addCardInHand(new Card(5));
        p.addCardInHand(new Card(60));

        action = new Action(null, null);
        assertFalse(action.isValid(p, e));
        for(ActionType type : ActionType.values()) {
            action = new Action(type, null);
            assertFalse(action.isValid(p, e));
        }

        action = new Action(null, new Card(60));
        assertFalse(action.isValid(p, e));

        /*
         * On vérifie que l'on ne peut pas ajouter une
         * carte que l'on ne possède pas
         */
        p = new Player(PlayerType.SUD);
        e = new Player(PlayerType.NORD);

        action = new Action(ActionType.ADD_ASC, new Card(5));
        assertFalse(action.isValid(p, e));
        p.addCardInHand(new Card(5));
        assertTrue(action.isValid(p, e));

        /*
         * On vérifie que l'on ne peut pas ajouter :
         * - une carte < sur la pile ASC : (La carte 20 sur la carte 25)
         * - une carte > sur la pile DESC : (La carte 40 sur la carte 35)
         * Mais que l'inverse est possible
         */
        p = new Player(PlayerType.SUD);
        e = new Player(PlayerType.NORD);
        p.addCardInHand(new Card(20));
        p.addCardInHand(new Card(40));
        p.getPileAsc().add(new Card(25));
        p.getPileDesc().add(new Card(35));

        action = new Action(ActionType.ADD_ASC, new Card(20));
        assertFalse(action.isValid(p, e));

        action = new Action(ActionType.ADD_DESC, new Card(40));
        assertFalse(action.isValid(p, e));

        action = new Action(ActionType.ADD_DESC, new Card(20));
        assertTrue(action.isValid(p, e));

        action = new Action(ActionType.ADD_ASC, new Card(40));
        assertTrue(action.isValid(p, e));

        /*
         * On vérifie les règles d'ajout +10 et -10
         *
         * (Scénario issu d'un bug)
         * NORD : ^[32] v[48] (m6p45)
         * cartes NORD { 13 19 34 40 53 58 }
         *
         * Impossible de faire : 58v 53v
         */
        p = new Player(PlayerType.SUD);
        e = new Player(PlayerType.NORD);
        p.addCardInHand(new Card(58));
        p.getPileDesc().add(new Card(48));

        action = new Action(ActionType.ADD_DESC, new Card(58));
        assertTrue(action.isValid(p, e));

        /*
         * On vérifie les règles d'ajout sur la pile adverse :
         * - ASC : Doit être inférieur
         * - DESC : Doit être supérieur
         */
        p = new Player(PlayerType.SUD);
        e = new Player(PlayerType.NORD);
        p.addCardInHand(new Card(40));
        p.addCardInHand(new Card(30));
        p.addCardInHand(new Card(25));
        p.addCardInHand(new Card(20));

        action = new Action(ActionType.ADD_ASC_ENEMY, new Card(20));
        assertFalse(action.isValid(p, e));
        e.getPileAsc().add(new Card(25));
        assertTrue(action.isValid(p, e));

        action = new Action(ActionType.ADD_DESC_ENEMY, new Card(40));
        assertFalse(action.isValid(p, e));
        e.getPileDesc().add(new Card(30));
        assertTrue(action.isValid(p, e));

    }

    // Getters & Setters ==========----------

    @Test
    public void getTargetPile(){

        Action action = new Action(ActionType.ADD_ASC, new Card(6));
        assertEquals(PileType.ASC, action.getTargetPile());

        action = new Action(null, new Card(6));
        assertNull(action.getTargetPile());

    }

}