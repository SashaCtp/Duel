package appli;

import appli.types.ActionType;
import appli.types.PileType;

public class Action {

    private ActionType actionType;
    private Card card;

    /**
     * Une action est la description d'un mouvement effectué par un joueur
     * Cette action peut ou pas être valide
     *
     * Une action valide indique qu'avec la configuration donnée, cette carte peut être posée
     * Si plusieurs cartes sont valides, l'ordre reste a valider par l'ActionBuffer
     * @param type Type d'action
     * @param card Carte utilisé dans l'action
     */
    public Action(ActionType type, Card card){
        this.actionType = type;

        if(card != null)
            this.card = new Card(card);
    }

    /**
     * Constructeur par copie
     */
    public Action(Action action){
        this(action.getType(), action.getCard());
    }

    /**
     * Créé une action à partir d'une chaîne de caractères (Parse string)
     * @param action Action demandé à faire par l'utilisateur
     * @throws IllegalArgumentException Erreur si la chaîne de caractère est incorrecte
     */
    public Action(String action) throws IllegalArgumentException {

        // On vient d'abord vérifier que la taille est correcte
        if (action.length() < 3 || action.length() > 4)
            throw new IllegalArgumentException("Taille de la chaîne invalide.");

        // On vérifie que si il y a un 4e caractère, il s'agît de '
        if (action.length() == 4 && action.charAt(3) != '\'')
            throw new IllegalArgumentException("Le dernier caractère est incorrect.");

        // On vient maintenant récupérer le nombre contenu dans la première partie de l'action
        int cardValue = 0;
        try {
            cardValue = Integer.parseInt(action.substring(0, 2));
        } catch (Exception e) {
            throw new IllegalArgumentException("La valeur de la carte doit être de format  \"00\".");
        }

        // On récupère le type d'action
        ActionType type;

        // On vérifie que le caractère désignant la pile est correct
        if (action.charAt(2) == '^') {
            if (action.length() == 4)
                type = ActionType.ADD_ASC_ENEMY;
            else
                type = ActionType.ADD_ASC;
        } else if (action.charAt(2) == 'v') {
            if (action.length() == 4)
                type = ActionType.ADD_DESC_ENEMY;
            else
                type = ActionType.ADD_DESC;
        } else
            throw new IllegalArgumentException("Aucune pile n'est sélectionnée.");

        // Si tout est correct on construit l'action
        this.actionType = type;
        this.card = new Card(cardValue);

    }

    /**
     * Vérifie si une action vise ou non la pile du joueur énemie
     * @return True si l'action est envers un joueur énemie, False sinon
     */
    public boolean isEnemyTarget(){

        if(this.actionType != null)
            return (this.actionType.equals(ActionType.ADD_ASC_ENEMY) || this.actionType.equals(ActionType.ADD_DESC_ENEMY));

        return false;

    }

    /**
     * Vérifie qu'une action est valide sans prendre en compte un ActionBuffer
     * @param player Le joueur qui exécute l'action
     */
    public boolean isValid(Player player, Player enemy){

        // On vérifie que le type d'action est correct
        if(this.getType() == null || this.getCard() == null)
            return false;

        // On vérifie que le joueur possède la carte
        if(!player.hasCardInHand(this.getCard()))
            return false;

        // Si l'action est vers l'ennemi, on vérifie que la carte peut être posée
        if(this.isEnemyTarget())
            return enemy.getPile(this.getTargetPile()).canAddEnemy(this.getCard());

        // Sinon
        return player.getPile(this.getTargetPile()).canAdd(this.getCard());

    }

    public boolean isValid(Player player, Player enemy, ActionBuffer actionBuffer){

        // Si on vérifie la validité de l'action sans actionBuffer
        // ou si l'actionBuffer est vide, cela revient à faire sans
        // ou si l'actionBuffer ne contient qu'une action vers un ennemi et que l'action n'est pas vers un ennemi
        if(actionBuffer == null
                || actionBuffer.getActions().size() == 0
                || (actionBuffer.getActions().size() == 1 && actionBuffer.getActions().get(0).isEnemyTarget()) && !this.isEnemyTarget())
            return this.isValid(player, enemy);

        if(this.getType() == null || this.getCard() == null)
            return false;

        if(!player.hasCardInHand(this.getCard()))
            return false;

        // Si l'action est vers l'ennemi, on vérifie que la carte peut être posée
        if(this.isEnemyTarget() && !enemy.getPile(this.getTargetPile()).canAddEnemy(this.getCard()))
            return false;

        // Vérification concernant l'action buffer ==========-----------

        // On vérifie si le joueur n'a pas déjà utilisé la carte
        if(actionBuffer.useCard(this.getCard()))
            return false;

        // Si il s'agit d'une action sur sa pile, on vérifie qu'on peut l'ajouter sur la pile temporaire
        if(this.getType().equals(ActionType.ADD_ASC) && actionBuffer.getTmpAsc().canAdd(this.getCard()))
            return true;

        if(this.getType().equals(ActionType.ADD_DESC) && actionBuffer.getTmpDesc().canAdd(this.getCard()))
            return true;

        // Si il s'agit d'une action sur une pile adverse, on vérifie qu'il n'existe pas déjà une action vers l'ennemi
        if(this.isEnemyTarget()) {
            for (Action a : actionBuffer.getActions())
                if (a.isEnemyTarget())
                    return false;

            return true;
        }

        return false;

    }

    // Getters & Setters ==========----------

    /**
     * Vérifie si une action vise ou non la pile du joueur énemie
     * @return True si l'action est envers un joueur énemie, False sinon
     */
    public PileType getTargetPile(){

        if(this.actionType == null)
            return null;

        if(this.actionType.equals(ActionType.ADD_ASC) ||  this.actionType.equals(ActionType.ADD_ASC_ENEMY))
            return PileType.ASC;
        else
            return PileType.DESC;

    }

    public ActionType getType() { return actionType; }

    public void setType(ActionType actionType) {

        if(actionType == null)
            throw new IllegalArgumentException("Le type d'action ne peut pas être nul");

        this.actionType = actionType;
    }

    public Card getCard() { return card; }

    public void setCard(Card card) {
        this.card = card;
    }
}
