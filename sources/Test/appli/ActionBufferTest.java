package appli;

import org.junit.jupiter.api.Test;

import appli.types.*;

import static org.junit.jupiter.api.Assertions.*;

class ActionBufferTest {

    @Test

    void add() {

        Player player = new Player(PlayerType.NORD);
        Player enemy = new Player(PlayerType.SUD);

        ActionBuffer actionBuffer = new ActionBuffer(player, enemy);

        Action action = new Action(ActionType.ADD_ASC, new Card(9));
        player.addCardInHand(new Card(9));
        assertDoesNotThrow(() -> actionBuffer.add(action));
        assertThrows(IllegalArgumentException.class ,() -> actionBuffer.add(action));

        // Test de l'encapsulation
        // action est déjà ajoutée dans l'ActionBuffer, avec une carte d'une valeur 9
        action.setCard(new Card(5));
        assertNotEquals(new Card(5), actionBuffer.getActions().get(0).getCard());

        // On essaye d'ajouter des actions non valides
        Action illegalAction = new Action(ActionType.ADD_ASC, new Card(4));
        assertThrows(Exception.class, () -> actionBuffer.add(illegalAction));
        illegalAction.setType(ActionType.ADD_ASC_ENEMY);
        assertThrows(Exception.class, () -> actionBuffer.add(illegalAction));
        illegalAction.setCard(null);
        assertThrows(Exception.class, () -> actionBuffer.add(illegalAction));

    }

    @Test
    void canAdd() {

        Player p;
        Player e;
        ActionBuffer actionBuffer;
        Action action;

        /*
         * On vérifie que l'on ne peut pas ajouter une
         * action qui n'est pas valide
         */
        p = new Player(PlayerType.SUD);
        e = new Player(PlayerType.NORD);
        actionBuffer = new ActionBuffer(p, e);

        action = new Action(null, null);
        assertFalse(actionBuffer.canAdd(action));
        action = new Action(null, new Card(20));
        assertFalse(actionBuffer.canAdd(action));

        /*
         * On vérifie que l'on ne peut pas ajouter deux fois la même carte
         * sur une pile ennemie
         */
        p = new Player(PlayerType.SUD);
        e = new Player(PlayerType.NORD);
        actionBuffer = new ActionBuffer(p, e);

        e.getPileAsc().add(new Card(31));
        p.addCardInHand(new Card(20));

        action = new Action(ActionType.ADD_ASC_ENEMY, new Card(20));
        assertTrue(action.isValid(p, e));
        assertTrue(actionBuffer.canAdd(action));
        actionBuffer.add(action);
        assertFalse(actionBuffer.canAdd(action));

        action.setCard(new Card(18));

        assertFalse(actionBuffer.canAdd(action));

        /*
         * On vérifie que l'on ne peut pas placer la même carte
         * deux fois sur une même pile
         */
        p = new Player(PlayerType.SUD);
        e = new Player(PlayerType.NORD);
        actionBuffer = new ActionBuffer(p, e);
        action = new Action(ActionType.ADD_ASC, new Card(10));

        p.addCardInHand(new Card(10));

        assertTrue(actionBuffer.canAdd(action));
        actionBuffer.add(action);
        assertFalse(actionBuffer.canAdd(action));

        /*
         * On vérifie que l'on ne peut pas placer la même
         * carte sur la pile ASC et DESC (ET ennemi)
         */
        p = new Player(PlayerType.SUD);
        e = new Player(PlayerType.NORD);
        ActionBuffer actionBuffer1 = new ActionBuffer(p, e);

        Action addDesc = new Action(ActionType.ADD_DESC, new Card(30));
        Action addAsc = new Action(ActionType.ADD_ASC, new Card(30));
        Action addEDesc = new Action(ActionType.ADD_ASC_ENEMY, new Card(30));
        Action addEAsc = new Action(ActionType.ADD_ASC_ENEMY, new Card(30));
        p.addCardInHand(new Card(30));

        assertTrue(actionBuffer1.canAdd(addAsc));
        assertDoesNotThrow(() -> actionBuffer1.add(addAsc));

        assertFalse(actionBuffer1.canAdd(addDesc));
        assertThrows(IllegalArgumentException.class ,() -> actionBuffer1.add(addDesc));

        assertFalse(actionBuffer1.canAdd(addEDesc));
        assertThrows(IllegalArgumentException.class ,() -> actionBuffer1.add(addEDesc));

        assertFalse(actionBuffer1.canAdd(addEAsc));
        assertThrows(IllegalArgumentException.class ,() -> actionBuffer1.add(addEAsc));

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
        ActionBuffer actionBuffer2 = new ActionBuffer(p, e);
        p.addCardInHand(new Card(53));
        p.addCardInHand(new Card(58));
        p.getPileDesc().add(new Card(48));

        Action action1 = new Action(ActionType.ADD_DESC, new Card(58));
        Action action2 = new Action(ActionType.ADD_DESC, new Card(53));
        assertTrue(actionBuffer2.canAdd(action1));
        assertDoesNotThrow(() -> actionBuffer2.add(action1));

        assertNotEquals(action1.getCard(), action2.getCard());

        assertTrue(actionBuffer2.canAdd(action2));
        assertDoesNotThrow(() -> actionBuffer2.add(action2));

    }

    @Test
    void clear() {

        Player player = new Player(PlayerType.NORD);
        Player enemy = new Player(PlayerType.SUD);

        ActionBuffer actionBuffer = new ActionBuffer(player, enemy);
        Action action = new Action(ActionType.ADD_ASC, new Card(9));
        player.addCardInHand(new Card(9));
        actionBuffer.add(action);

        enemy.getPileAsc().add(new Card(31));
        player.addCardInHand(new Card(20));
        Action action2 = new Action(ActionType.ADD_ASC_ENEMY, new Card(20));
        actionBuffer.add(action2);

        assertEquals(2, actionBuffer.getActions().size());

        actionBuffer.clear();

        assertEquals(0, actionBuffer.getActions().size());

    }

    @Test
    void containsEnemyAction(){

        Player player = new Player(PlayerType.NORD);
        Player enemy = new Player(PlayerType.SUD);

        ActionBuffer actionBuffer = new ActionBuffer(player, enemy);

        assertFalse(actionBuffer.containsEnemyAction());

        player.addCardInHand(new Card(5));
        enemy.getPileAsc().add(new Card(10));

        Action action = new Action(ActionType.ADD_ASC_ENEMY, new Card(5));
        actionBuffer.add(action);

        assertTrue(actionBuffer.containsEnemyAction());

    }

}