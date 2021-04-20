package appli;

public class Card {

    private Integer value;

    /**
     * Constructeur
     * @param value Valeur de la carte (1 <= valeur <= 60)
     * @throws IllegalArgumentException Valeur incorrecte
     */
    public Card(Integer value) throws IllegalArgumentException{

        if(value < 1 || value > 60)
            throw new IllegalArgumentException("La valeur d'une carte appartient à l'intervalle [1,60]");

        this.value = value;

    }

    /**
     * Constructeur par copie
     */
    public Card(Card card){

        this(card.getValue());

    }

    /**
     * Retourne la carte sous la forme de deux digits
     * @return string Carte sous le format de deux nombres
     */
    @Override
    public String toString(){

        return String.format("%02d", this.getValue());

    }

    @Override
    public boolean equals(Object obj){

        if(obj == this)
            return true;
        if(obj == null)
            return false;
        if(!(obj instanceof Card))
            return false;

        Card c = (Card) obj;
        return (this.getValue().equals(c.getValue()));

    }

    /**
     * Vérifie si la carte est plus petite qu'une autre
     * @param c Carte avec laquelle on compare
     * @return True : La carte est plus petite, False sinon
     */
    public boolean isSmallerThan(Card c){

        return (this.getValue() < c.getValue());

    }

    // Getters and Setter ==========----------
    public Integer getValue() {
        return value;
    }
}
