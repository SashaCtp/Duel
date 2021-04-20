package appli;

import appli.types.PlayerType;

public class Application {

    public static void main(String[] args){

        Game game = new Game(new Player(PlayerType.NORD), new Player(PlayerType.SUD));

        game.play();

        try {
            Player winner = game.getWinner();
            System.out.println("partie finie, " + winner.getType() + " a gagné");
        }catch (Exception e){
            e.printStackTrace();
        }

    }

}
