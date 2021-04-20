package appli;

import appli.types.ActionType;
import appli.types.PileType;
import appli.types.PlayerType;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.Scanner;

public class Player {

    private PlayerType playerType;
    private Pioche pioche;
    private Pile pileAsc;
    private Pile pileDesc;
    private ArrayList<Card> hand;

    public Player(PlayerType playerType){

        this.playerType = playerType;
        this.pioche = new Pioche();
        this.pileAsc = new Pile(PileType.ASC);
        this.pileDesc = new Pile(PileType.DESC);
        this.hand = new ArrayList<>();

    }

    public Player(){

        this.pioche = new Pioche();
        this.pileAsc = new Pile(PileType.ASC);
        this.pileDesc = new Pile(PileType.DESC);
        this.hand = new ArrayList<>();

    }

    /**
     * Vérifie si un joueur peut jouer (i.e. poser au moins 2 cartes)
     * @return True : Le joueur peut jouer, False : Le joueur ne peut pas jouer
     */
    public boolean canPlay(Player enemy) {

        return (this.getActionsUsingCards(2, enemy).size() > 0);

    }
    /**
     * Retourne, pour un joueur et une liste préalable d'actions
     * les coups faisables avec les cartes restantes
     * @param actionBuffer Buffer des actions effectuées
     * @return Liste des coups possibles
     */
    public ArrayList<ActionBuffer> getPossibleActions(ActionBuffer actionBuffer){

        ArrayList<ActionBuffer> buffers = new ArrayList<>();

        for(Card card : this.getHand()){

            if(!actionBuffer.useCard(card)){

                for(ActionType type : ActionType.values()){

                    Action nextAction = new Action(type, card);

                    if(actionBuffer.canAdd(nextAction)){

                        ActionBuffer newBuffer = new ActionBuffer(actionBuffer);
                        newBuffer.add(nextAction);
                        buffers.add(newBuffer);

                    }

                }

            }

        }

        return buffers;

    }

    /**
     * Retourne une liste de combinaisons d'actions permettant
     * d'utiliser un certain nombre de cartes de la main du
     * (Cette fonction permet de déterminer si un joueur peu jouer ou non)
     * @param cardNumber Nombre de carte devant être utilisées
     * @see Player#getPossibleActions(ActionBuffer)
     */
    public ArrayList<ActionBuffer> getActionsUsingCards(int cardNumber, Player enemy){

        ArrayList<ActionBuffer> bufferList = new ArrayList<>();
        bufferList.add(new ActionBuffer(this, enemy));

        for(int i = 0; i < cardNumber; i++){

            ArrayList<ActionBuffer> tmpList = new ArrayList<>();

            for(ActionBuffer buffer : bufferList){

                tmpList.addAll(this.getPossibleActions(buffer));

            }

            bufferList = new ArrayList<>(tmpList);

        }

        return bufferList;

    }

    /**
     * Gère les coups du joueur
     * @param enemy Joueur ennemi
     */
    public void playTurn(Player enemy){

        boolean correctActions = false;
        boolean error = false;

        ActionBuffer actionBuffer = new ActionBuffer(this, enemy);

        while(!correctActions){

            ArrayList<String> actionStrings = this.typeActions(error);
            actionBuffer.clear();
            error = false;

            if(actionStrings.isEmpty() || actionStrings.size() < 2){
                error = true;
                continue;
            }

            for(String actionString : actionStrings){

                try {

                    Action action = new Action(actionString);

                    if(actionBuffer.canAdd(action)) {
                        actionBuffer.add(action);
                    }else {
                        error = true;
                        break;
                    }

                }catch (IllegalArgumentException e){
                    error = true;
                    break;
                }

            }

            if(!error)
                correctActions = true;

        }

        try{

            this.executeActionBuffer(actionBuffer);
            int drawnCards = this.drawEndRoundCards(actionBuffer);

            System.out.println(actionBuffer.getActions().size() + " cartes posées, " + drawnCards + " cartes piochées");

        }catch (Exception e){
            e.printStackTrace();
        }

    }

    /**
     * Demande les coups que veut jouer le joueur
     */
    public ArrayList<String> typeActions(boolean previousError){

        if(previousError)
            System.out.print("#");
        System.out.print("> ");

        ArrayList<String> actionStrings = new ArrayList<>();

        Scanner scanner1 = new Scanner(System.in);

        if(scanner1.hasNextLine()){

            Scanner scanner2 = new Scanner(scanner1.nextLine());

            while(scanner2.hasNext())
                actionStrings.add(scanner2.next());

        }

        return actionStrings;

    }

    // Draw Card ==========----------

    /**
     * Fait piocher un certain nombre de carte au joueur et trie son paquet
     * @param n Le nombre de cartes à piocher (Si > au nbr de cartes dans la pioche, renvoie une exception)
     */
    public void drawCard(int n) throws Exception{

        if(n > this.getPioche().size())
            throw new Exception("Il n'y a pas assez de cartes dans la pioche !");
        else{
            for(int i = 0; i < n; i++)
                this.addCardInHand(this.getPioche().drawCard());
        }

    }

    /**
     * Pioche le nombre de carte nécessaire
     * @param actionBuffer Action buffer contenant les actions exécutées
     */
    public int drawEndRoundCards(ActionBuffer actionBuffer) throws Exception {

        int drawnCards = 2;

        if(actionBuffer.containsEnemyAction())
            drawnCards = 6 - this.getHand().size();

        if(drawnCards > this.getPioche().size())
            drawnCards = this.getPioche().size();

        if(drawnCards > 0)
            this.drawCard(drawnCards);

        return drawnCards;

    }

    // Actions ==========----------

    /**
     * Execute l'action
     * @param action Action a exécuter
     * @param enemy Joueur énemi
     * @throws Exception L'action n'a pas pu s'exécuter correctement
     */
    public void executeAction(Action action, Player enemy) throws Exception{

        this.removeCardFromHand(action.getCard());

        if(action.isEnemyTarget())
            enemy.getPile(action.getTargetPile()).addEnemy(action.getCard());
        else
            this.getPile(action.getTargetPile()).add(action.getCard());

    }

    /**
     * Execute l'action
     * @param buffer Ensemble des actions à exécuter
     * @throws Exception L'action n'a pas pu s'exécuter correctement
     */
    public void executeActionBuffer(ActionBuffer buffer) throws Exception{

        for(Action action : buffer.getActions())
            this.executeAction(action, buffer.getEnemy());

    }

    // Hand ==========----------

    /**
     * Ajoute une carte dans la main du joueur & ordonne la liste de cartes
     * @param card Carte à ajouter
     */
    public void addCardInHand(Card card){

        if(!this.hasCardInHand(card)) {
            this.hand.add(card);
            this.sortCardsInHand();
        }

    }

    /**
     * Enlève une carte dans la main du joueur
     * @param card Carte à enlever
     *             La carte doit être présente dans la main du joueur
     */
    public void removeCardFromHand(Card card) throws Exception{

        if(!this.hasCardInHand(card))
            throw new Exception("Le joueur ne possède pas cette carte.");
        else{

            this.hand.remove(card);

            for(int i = 0; i < this.hand.size(); i++){

                if(this.hand.get(i).equals(card)) {
                    this.hand.remove(i);
                    break;
                }

            }

        }
    }

    /**
     * Vérifie si un joueur possède une carte dans sa main
     * @param card Carte à vérifier
     * @return True : il possède cette carte dans sa main, False sinon
     */
    public boolean hasCardInHand(Card card){

        for(Card c : this.getHand())
            if(c.equals(card))
                return true;

        return false;

    }

    /**
     * Tri les cartes en main dans l'ordre croissant
     */
    public void sortCardsInHand(){

        this.hand.sort(new Comparator<Card>() {
            @Override
            public int compare(Card o1, Card o2) {

                return o1.getValue() - o2.getValue();

            }
        });

    }

    // Display ==========----------

    /**
     * Affiche les cartes en main
     */
    public String handToString(){

        StringBuilder sb = new StringBuilder("cartes ");
        sb.append(this.getType());
        sb.append(" { ");

        for(Card card : this.hand) {
            sb.append(card.toString());
            sb.append(" ");
        }

        sb.append("}");

        return sb.toString();
    }

    /**
     * Affiche les piles, le nombre de cartes en main et le nombre de cartes dans la pioche
     */
    @Override
    public String toString(){

        StringBuilder sb = new StringBuilder();
        sb.append(this.getDisplayName());
        sb.append(" ");
        sb.append(this.getPileAsc());
        sb.append(" ");
        sb.append(this.getPileDesc());
        sb.append(" ");
        sb.append("(m");
        sb.append(this.getHand().size());
        sb.append("p");
        sb.append(this.getPioche().size());
        sb.append(")");

        return sb.toString();

    }

    /**
     * Retourne le nom du joueur avec des espaces pour conserver l'alignement dans l'affichage
     * @return Nom d'affichage
     */
    private String getDisplayName() {

        // On récupère la taille du nom le plus grand (permet d'adapter les noms facilement)
        int size = 0;
        for (PlayerType type : PlayerType.values()){
            if (type.toString().length() > size)
                size = type.toString().length();
        }

        if(size == this.getType().toString().length())
            return this.getType().toString();

        StringBuilder sb = new StringBuilder(this.getType().toString());
        for(int i = 0; i < size-this.getType().toString().length(); i++)
            sb.append(" ");

        return sb.toString();

    }

    // Getters and Setters ==========----------

    public PlayerType getType() { return playerType; }

    public Pioche getPioche() { return pioche; }

    public Pile getPileAsc() { return pileAsc; }

    public Pile getPileDesc() { return pileDesc; }

    /**
     * Retourne la pile en fonction du type de pile
     * @param type Type de la pile
     */
    public Pile getPile(PileType type){
        if(type.equals(PileType.ASC))
            return this.getPileAsc();
        else if(type.equals(PileType.DESC))
            return this.getPileDesc();

        return null;
    }

    public ArrayList<Card> getHand() { return hand; }

}
