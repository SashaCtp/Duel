package appli;

import appli.types.ActionType;
import appli.types.PlayerType;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class PlayerTest {

    @Test
    @DisplayName("Vérification qu'un joueur peut jouer")
    void canPlay() {

        Player p = new Player(PlayerType.NORD);
        Player e = new Player(PlayerType.SUD);

        assertFalse(p.canPlay(e));

        p.addCardInHand(new Card(5));

        // Rappel, on peut jouer que si l'on peut poser 2 cartes
        assertFalse(p.canPlay(e));

        p.addCardInHand(new Card(55));

        assertTrue(p.canPlay(e));

        /*
         * Verifie que dans le cas suivant un joueur
         * peut jouer
         */
        p = new Player(PlayerType.NORD);
        e = new Player(PlayerType.SUD);

        p.getPileAsc().add(new Card(59));
        p.getPileDesc().add(new Card(40));

        p.addCardInHand(new Card(50));
        p.addCardInHand(new Card(48));
        p.addCardInHand(new Card(46));

        assertTrue(p.canPlay(e));

    }

    // Test Draw Card ==========----------

    @Test
    @DisplayName("Pioche une carte")
    public void testDrawCard(){

        Player p = new Player();

        assertEquals(0, p.getHand().size());

        // Drawing 1 card
        assertDoesNotThrow(() -> p.drawCard(1));
        assertEquals(1, p.getHand().size());

        // Drawing 57 cards from it's drawing pile
        assertDoesNotThrow(() -> p.drawCard(57));
        assertEquals(58, p.getHand().size());

        assertThrows(Exception.class, () -> p.drawCard(1));

    }

    @Test
    @DisplayName("Pioche automatiquement à la fin du tour")
    public void testDrawEndroundCard() {

        Player p1 = new Player(PlayerType.NORD);
        Player e1 = new Player(PlayerType.SUD);

        assertEquals(0, p1.getHand().size());

        p1.addCardInHand(new Card(2));
        p1.addCardInHand(new Card(50));
        p1.addCardInHand(new Card(58));

        /*
         * Pioche 2 cartes à la fin d'un tour
         * lorsque 3 cartes ont été posées
         * sans avoir posé de carte sur la pile ennemie
         */
        ActionBuffer actionBuffer = new ActionBuffer(p1, e1);
        assertDoesNotThrow(() -> actionBuffer.add(new Action(ActionType.ADD_ASC, new Card(2))));
        assertDoesNotThrow(() -> actionBuffer.add(new Action(ActionType.ADD_DESC, new Card(58))));
        assertDoesNotThrow(() -> actionBuffer.add(new Action(ActionType.ADD_DESC, new Card(50))));
        assertDoesNotThrow(() -> p1.executeActionBuffer(actionBuffer));

        assertDoesNotThrow(() -> assertEquals(2, p1.drawEndRoundCards(actionBuffer)));
        assertEquals(2, p1.getHand().size());

        /*
         * La partie est à l'état suivant :
         *  NORD ^[01] v[50] m5p1
         *  SUD  ^[05] v[60] ----
         *
         * Si il pose 3 cartes, dont une sur une pile ennemie
         * il piochera 4 cartes pour remonter à 6
         */
        Player p2 = new Player(PlayerType.NORD);
        Player e2 = new Player(PlayerType.SUD);
        ActionBuffer actionBuffer2 = new ActionBuffer(p2, e2);

        p2.addCardInHand(new Card(1));
        p2.addCardInHand(new Card(2));
        p2.addCardInHand(new Card(3));
        p2.addCardInHand(new Card(4));
        p2.addCardInHand(new Card(45));

        assertEquals(5, p2.getHand().size());

        e2.getPileAsc().add(new Card(5));

        assertDoesNotThrow(() -> actionBuffer2.add(new Action(ActionType.ADD_ASC_ENEMY, new Card(3))));
        assertDoesNotThrow(() -> actionBuffer2.add(new Action(ActionType.ADD_ASC, new Card(4))));
        assertDoesNotThrow(() -> actionBuffer2.add(new Action(ActionType.ADD_DESC, new Card(45))));

        assertDoesNotThrow(() -> p2.executeActionBuffer(actionBuffer2));

        assertDoesNotThrow(() -> assertEquals(4, p2.drawEndRoundCards(actionBuffer2)));
        assertEquals(6, p2.getHand().size());

        /*
         * La partie est à l'état suivant :
         * NORD ^[01] v[60] m2p1
         * SUD  ^[01] v[60] ----
         *
         * Si il reste 1 carte dans sa pioche, il piochera au maximum 1 carte
         */
        Player p3 = new Player(PlayerType.NORD);
        Player e3 = new Player(PlayerType.SUD);
        ActionBuffer actionBuffer3 = new ActionBuffer(p3, e3);

        // On laisse une seule carte dans la pioche
        while (p3.getPioche().size() != 1){
            try {
                p3.getPioche().drawCard();
            }catch (Exception exception) {
                exception.printStackTrace();
            }
        }
        assertEquals(1, p3.getPioche().size());

        p3.addCardInHand(new Card(5));
        p3.addCardInHand(new Card(6));

        assertDoesNotThrow(() -> actionBuffer3.add(new Action(ActionType.ADD_ASC, new Card(5))));
        assertDoesNotThrow(() -> actionBuffer3.add(new Action(ActionType.ADD_ASC, new Card(6))));
        assertDoesNotThrow(() -> p3.executeActionBuffer(actionBuffer3));
        assertDoesNotThrow(() -> assertEquals(1, p3.drawEndRoundCards(actionBuffer3)));
        assertEquals(1, p3.getHand().size());

        /*
         * La partie est à l'état suivant :
         * NORD ^[01] v[60] m4p0
         * SUD  ^[03] v[60] ----
         *
         * Il n'y a plus de cartes dans la pioche
         * A la fin du tour, il ne piochera donc pas de carte
         */
        Player p4 = new Player(PlayerType.NORD);
        Player e4 = new Player(PlayerType.SUD);
        ActionBuffer actionBuffer4 = new ActionBuffer(p4, e4);

        // On vide la pioche
        while (!p4.getPioche().isEmpty()){
            try {
                p4.getPioche().drawCard();
            }catch (Exception exception) {
                exception.printStackTrace();
            }
        }

        p4.addCardInHand(new Card(1));
        p4.addCardInHand(new Card(2));
        p4.addCardInHand(new Card(7));
        p4.addCardInHand(new Card(9));

        e4.getPileAsc().add(new Card(3));

        assertDoesNotThrow(() -> actionBuffer4.add(new Action(ActionType.ADD_ASC_ENEMY, new Card(2))));
        assertDoesNotThrow(() -> actionBuffer4.add(new Action(ActionType.ADD_ASC, new Card(7))));
        assertDoesNotThrow(() -> actionBuffer4.add(new Action(ActionType.ADD_ASC, new Card(9))));
        assertDoesNotThrow(() -> p4.executeActionBuffer(actionBuffer4));
        assertDoesNotThrow(() -> assertEquals(0, p4.drawEndRoundCards(actionBuffer4)));
        assertEquals(1, p4.getHand().size());

    }

    // Test action ==========----------

    @Test
    @DisplayName("Exécution d'une action par un joueur")
    void execute(){

        Player p = new Player(PlayerType.NORD);
        Player e = new Player(PlayerType.SUD);

        Action action = new Action(ActionType.ADD_ASC, new Card(5));
        assertThrows(Exception.class, () -> p.executeAction(action, e));
        p.addCardInHand(new Card(5));
        assertEquals(1, p.getHand().size());

        assertDoesNotThrow(() -> p.executeAction(action, e));

        assertEquals(new Card(5), p.getPileAsc().getTop());
        assertFalse(p.hasCardInHand(action.getCard()));
        assertEquals(0, p.getHand().size());

        Action action1 = new Action(ActionType.ADD_ASC_ENEMY, null);
        assertThrows(Exception.class, () -> p.executeAction(action1, e));
    }

    @Test
    @DisplayName("Execution de toutes les actions d'un ActionBuffer")
    void executeAll(){

        Player player = new Player(PlayerType.NORD);
        Player enemy = new Player(PlayerType.SUD);
        ActionBuffer actionBuffer = new ActionBuffer(player, enemy);

        Action action1 = new Action(ActionType.ADD_ASC, new Card(5));
        player.addCardInHand(new Card(5));
        actionBuffer.add(action1);

        player.addCardInHand(new Card(6));
        enemy.getPileAsc().add(new Card(10));
        Action action2 = new Action(ActionType.ADD_ASC_ENEMY, new Card(6));
        actionBuffer.add(action2);

        assertDoesNotThrow(() -> player.executeActionBuffer(actionBuffer));

        assertEquals(5, player.getPileAsc().getTop().getValue());
        assertEquals(6, enemy.getPileAsc().getTop().getValue());

    }

    // Hand ==========----------

    @Test
    @DisplayName("Ajoute une carte en main")
    public void addCardInHand(){

        Player p = new Player(null);
        p.addCardInHand(new Card(42));

        assertEquals(1, p.getHand().size());
        assertEquals(new Card(42), p.getHand().get(0));

        /*
         * On vérifie que les cartes sont bien triées à l'ajout de nouvelles cartes
         */
        p.addCardInHand(new Card(5));
        assertEquals(new Card(5), p.getHand().get(0));
        assertEquals(new Card(42), p.getHand().get(1));

        p.addCardInHand(new Card(59));
        assertEquals(new Card(5), p.getHand().get(0));
        assertEquals(new Card(42), p.getHand().get(1));
        assertEquals(new Card(59), p.getHand().get(2));

    }

    @Test
    @DisplayName("Enlève une carte de la main")
    public void removeCardFromHand(){

        Player p = new Player(null);

        // Tries to remove a card the player do not own
        assertThrows(Exception.class ,() -> p.removeCardFromHand(new Card(42)));

        p.addCardInHand(new Card(42));

        // Tries to remove a card the player owns
        assertDoesNotThrow(() -> p.removeCardFromHand(new Card(42)));

        assertEquals(0, p.getHand().size());

    }

    @Test
    @DisplayName("Vérification de la présence d'une carte en main")
    public void hasCardInHand(){

        Player p = new Player(null);
        assertFalse(p.hasCardInHand(new Card(5)));
        assertFalse(p.hasCardInHand(new Card(1)));

        p.addCardInHand(new Card(42));

        assertTrue(p.hasCardInHand(new Card(42)));
        assertFalse(p.hasCardInHand(new Card(41)));

    }

    @Test
    @DisplayName("Fonction de tri des cartes")
    public void sortCardsInHand(){

        Player p = new Player(PlayerType.SUD);
        p.getHand().add(new Card(8));
        p.getHand().add(new Card(55));
        p.getHand().add(new Card(2));

        assertEquals(8, p.getHand().get(0).getValue());
        assertEquals(55, p.getHand().get(1).getValue());
        assertEquals(2, p.getHand().get(2).getValue());

        p.sortCardsInHand();

        assertEquals(2, p.getHand().get(0).getValue());
        assertEquals(8, p.getHand().get(1).getValue());
        assertEquals(55, p.getHand().get(2).getValue());

    }
}