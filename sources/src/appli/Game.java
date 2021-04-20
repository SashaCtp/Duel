package appli;

import appli.types.PlayerType;

public class Game {

    private Player[] players; // Liste de taille 2 : [NORD,SUD]
    private Player currentPlayer;
    boolean finished = false;

    /**
     * Constructeur de Game
     * @param player1 Joueur 1
     * @param player2 Joueur 2
     * Les deux joueurs doivent avoir un type différent (NORD et SUD)
     */
    public Game(Player player1, Player player2){

        this.players = new Player[2];

        if(player1.getType() == null || player2.getType() == null)
            throw new IllegalArgumentException("Les joueurs doivent avoir un type défini");
        else if(player1.getType().equals(player2.getType()))
            throw new IllegalArgumentException("Les joueurs doivent être d'un type différent l'un de l'autre");

        if(player1.getType().equals(PlayerType.NORD)){
            this.players[0] = player1;
            this.players[1] = player2;
        }else{
            this.players[0] = player2;
            this.players[1] = player1;
        }

        for(Player p : this.players){
            try {
                p.drawCard(6);
            }catch (Exception e){
                e.printStackTrace();
            }
        }

        this.currentPlayer = players[0];

    }

    /**
     * Lance la boucle de jeu
     */
    public void play(){

        try {

            while(!isFinished()){

                for(Player p : players)
                    System.out.println(p.toString());

                System.out.println(currentPlayer.handToString());

                currentPlayer.playTurn(getEnemy(currentPlayer));

                // On passe au joueur suivant
                nextPlayer();
            }

            for(Player p : players)
                System.out.println(p.toString());

            System.out.println(currentPlayer.handToString());

            // On place le gagnant dans la position courante
            nextPlayer();

        }catch (Exception exception) {
            exception.printStackTrace();
        }

    }

    /**
     * Change le joueur courant
     */
    public void nextPlayer() throws Exception{

        this.currentPlayer = this.getEnemy(this.currentPlayer);

    }
    /**
     * Vérifie si la partie est terminée ou non
     * @return True si la partie est terminée, False sinon
     */
    public boolean isFinished() throws Exception{

        return !currentPlayer.canPlay(getEnemy(currentPlayer));

    }

    /**
     * Retourne le joueur adverse
     * @param p Joueur dont on veut obtenir l'énemi
     * @return Joueur adverse
     */
    private Player getEnemy(Player p) throws Exception{

        if(p == null)
            throw new IllegalArgumentException("Le joueur doit être différent de null");

        if(this.players.length != 2)
            throw new Exception("La partie ne contient pas deux joueurs");

        if(this.players[0] == p)
            return this.players[1];

        return this.players[0];

    }

    /**
     * Retourne le gagnant de la partie
     * @return Gagnant de la partie
     */
    public Player getWinner(){ return currentPlayer; }

}
