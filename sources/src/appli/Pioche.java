package appli;

import java.util.ArrayList;
import java.util.Random;

public class Pioche {

    private ArrayList<Card> cardList;

    public Pioche(){

        this.cardList = new ArrayList<>();

        for(int i = 2; i <= 59; i++)
            this.cardList.add(new Card(i));

    }

    /**
     * Prend la carte en haut de la pioche
     */
    public Card drawCard() throws Exception{

        Random random = new Random();

        if(!this.isEmpty()){

            int index = random.nextInt(this.cardList.size());

            Card card = this.cardList.get(index);
            this.cardList.remove(index);

            return card;

        }else{

            throw new Exception("La pioche est vide !");

        }

    }

    /**
     * Permet de savoir si la pioche est vide ou non
     * @return True : La pioche est vide, False : elle contient encore des cartes
     */
    public boolean isEmpty() { return this.cardList.isEmpty(); }

    /**
     * Retourne la taille de la pioche
     * @return Taille de la pioche
     */
    public int size(){ return this.cardList.size(); }

    // Getters and Setter ==========-----------

    /**
     * Permet d'obtenir la liste des cartes de la pioche
     * @return Liste des cartes
     */
    public ArrayList<Card> getCardList() {

        return this.cardList;

    }

}
