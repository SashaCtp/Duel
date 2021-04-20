package appli;

import appli.types.PileType;

import java.util.ArrayList;

public class Pile {

    private PileType pileType;
    private ArrayList<Card> cards;

    public Pile(PileType type){

        cards = new ArrayList<>();
        this.pileType = type;

        if(this.pileType.equals(PileType.ASC))
            cards.add(new Card(1));
        else if(this.pileType.equals(PileType.DESC))
            cards.add(new Card(60));

    }

    /**
     * Constructeur par copie
     * @param pile Pile a copier
     */
    public Pile(Pile pile){

        this.pileType = pile.pileType;
        this.cards = new ArrayList<>(pile.cards);

    }

    /**
     * Vérifie si une carte peut être ajoutée à la pile (ascendante ou descendante)
     * @param card Carte à vérifier
     * @return True : On peut l'ajouter, False sinon
     */
    public boolean canAdd(Card card){

        if(this.getPileType().equals(PileType.ASC)){

            if(this.getTop().isSmallerThan(card))
                return true;

            return (card.getValue() == (this.getTop().getValue() - 10));

        }else{

            if(card.isSmallerThan(this.getTop()))
                return true;

            return (card.getValue() == (this.getTop().getValue() + 10));

        }

    }

    /**
     * Vérifie si une carte énemie peut être ajoutée à la pile
     * @param card Carte à vérifier
     * @return True : On peut l'ajouter, False sinon
     */
    public boolean canAddEnemy(Card card){

        return ((this.getPileType().equals(PileType.ASC) && card.getValue() < this.getCards().get(this.getCards().size()-1).getValue())
                || this.getPileType().equals(PileType.DESC) && card.getValue() > this.getCards().get(this.getCards().size()-1).getValue());

    }


    /**
     * Ajoute une carte à la pile
     * @param card Carte à ajouter (La carte doit pouvoir être ajoutée à la pile)
     * @see Pile#canAdd(Card)
     */
    public void add(Card card){

        if(this.canAdd(card))
            this.cards.add(new Card(card));

    }

    /**
     * Ajoute une carte à la pile
     * @param card Carte à ajouter (La carte doit pouvoir être ajoutée à la pile)
     * @see Pile#canAdd(Card)
     */
    public void addEnemy(Card card){

        if(this.canAddEnemy(card))
            this.cards.add(card);

    }

    /**
     * Affiche la pile selon le standard du projet
     */
    @Override
    public String toString(){

        StringBuilder stringBuilder = new StringBuilder();

        if(this.getPileType().equals(PileType.ASC))
            stringBuilder.append("^");
        else
            stringBuilder.append("v");

        stringBuilder.append("[");
        stringBuilder.append(this.cards.get(this.cards.size()-1).toString());
        stringBuilder.append("]");

        return stringBuilder.toString();

    }

    // Getters and Setter ==========----------
    public PileType getPileType(){
        return this.pileType;
    }

    public ArrayList<Card> getCards(){
        return this.cards;
    }

    public Card getTop(){
        if(this.cards.size() == 0)
            return null;

        return this.cards.get(this.cards.size() - 1);
    }
}
