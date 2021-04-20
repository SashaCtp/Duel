package appli;

import appli.types.ActionType;
import appli.types.PileType;

import java.util.ArrayList;

public class ActionBuffer {

    private Player player;
    private Player enemy;

    private ArrayList<Action> actions;

    private Pile tmpAsc;
    private Pile tmpDesc;

    /**
     * Un ActionBuffer est une liste d'actions VALIDES en attente d'exécutions
     *
     * Son rôle et d'assurer :
     * - la non redondance des actions (utilisation de d'une même carte à plusieurs reprises)
     * - d'exécuter les actions dans le bon ordre (et donc la validité des entrées des joueurs)
     *
     * @param player Joueur courant
     * @param enemy ennemi du joueur courant
     */
    public ActionBuffer(Player player, Player enemy){

        this.actions = new ArrayList<>();
        this.player = player;
        this.enemy = enemy;

        this.tmpAsc = new Pile(PileType.ASC);
        this.tmpDesc = new Pile(PileType.DESC);

    }

    /**
     * Constructeur par copie
     */
    public ActionBuffer(ActionBuffer buffer){

        this.actions = new ArrayList<>(buffer.getActions());
        this.player = buffer.player;
        this.enemy = buffer.enemy;

        this.tmpAsc = new Pile(buffer.getTmpAsc());
        this.tmpDesc = new Pile(buffer.getTmpDesc());

    }

    /**
     * Ajoute une action au buffer
     * @param action Action à ajouter
     */
    public void add(Action action) throws IllegalArgumentException{

        if(this.canAdd(action)){

            Action actionCopy = new Action(action);

            this.actions.add(actionCopy);

            if(actionCopy.getType().equals(ActionType.ADD_ASC))
                tmpAsc.add(actionCopy.getCard());
            else if(actionCopy.getType().equals(ActionType.ADD_DESC))
                tmpDesc.add(actionCopy.getCard());

        }else
            throw new IllegalArgumentException("L'action n'est pas valide");

    }

    /**
     * Vérifie si une action peut être ajoutée au buffer
     * @param action Action que l'on veut vérifier
     */
    public boolean canAdd(Action action){

        return action.isValid(this.player, this.enemy, this);

    }

    /**
     * Efface les actions contenues dans le buffer
     */
    public void clear(){

        this.actions = new ArrayList<>();
        this.tmpAsc = new Pile(PileType.ASC);
        this.tmpDesc = new Pile(PileType.DESC);

    }

    /**
     * Vérifie si l'action buffer contient une action envers le joueur énemi
     * @return True si elle contient, False sinon
     */
    public boolean containsEnemyAction(){

        for(Action action : this.getActions()){

            if(action.isEnemyTarget())
                return true;

        }

        return false;

    }

    /**
     * Vérifie si une action dans le buffer utilise une carte
     * @param card Carte à vérifier
     * @return True : La carte est utilisée, False sinon
     */
    public boolean useCard(Card card){

        for(Action a : this.getActions()) {
            if (a.getCard().equals(card))
                return true;
        }

        return false;

    }

    @Override
    public String toString(){

        StringBuilder str = new StringBuilder("=>");

        for(Action a : this.getActions()){

            str.append(" ");
            str.append(a.getCard().toString());

            switch (a.getType()) {
                case ADD_ASC:
                    str.append("^");
                    break;
                case ADD_DESC:
                    str.append("v");
                    break;
                case ADD_ASC_ENEMY:
                    str.append("^'");
                    break;
                case ADD_DESC_ENEMY:
                    str.append("v'");
                    break;
            }

        }

        return str.toString();

    }

    // Getters & Setter ==========----------
    public ArrayList<Action> getActions(){ return this.actions; }

    public Pile getTmpAsc(){ return tmpAsc; }

    public Pile getTmpDesc(){ return tmpDesc; }

    public Player getEnemy(){ return enemy; }

}
