import java.util.*;

public class RiskGame {
    private Board board;
    private static final int[] armySize = {50,35,30,25,20};
    private CommandWords command;

    /**
     * Creates an instance of the Risk game
     */
    public RiskGame()
    {
        board = new Board();
        command = new CommandWords();
    }

    public void attack(Country A, Country B, int numOfAttackers) {

    }

    public void playGame() {
        Scanner sc = new Scanner(System.in);
        System.out.println("Welcome to Risk! Please enter how many players are playing: ");
        int numPlayers = sc.nextInt();
        for (int i = 0; i < numPlayers; i++) {
            board.addPlayer(new Player("", armySize[numPlayers-2], i+1)); // creates players for the game
        }
        board.randomizePlayers(); // randomizes player order
        board.buildMap(); // adds all countries to map
        board.placePlayers(numPlayers); // place players randomly on the map
        //board.setAdjacentCountries();
        command.showCommads();
    }

    public static void main (String[] args) {
        RiskGame game = new RiskGame();
        game.playGame();
    }
}
