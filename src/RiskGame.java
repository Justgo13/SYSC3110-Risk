import java.util.*;

public class RiskGame {
    private Board board;
    private static final int[] armySize = {50,35,30,25,20};
    private Parser parser;
    private int turnIndex;

    /**
     * Creates an instance of the Risk game
     */
    public RiskGame()
    {
        board = new Board();

        parser = new Parser();
        turnIndex = 0;
    }

    public void endTurn(){
        // cycles through the turns
        turnIndex = (1 + turnIndex) % board.getNumOfPlayers();
    }

    public void attack(Country A, Country B, int numOfAttackers) {

    }

    public void playGame() {
        Scanner sc = new Scanner(System.in);
        System.out.println("Welcome to Risk! Please enter how many players are playing: ");
        int numPlayers = sc.nextInt();
        board.setNumOfPlayers(numPlayers);
        for (int i = 0; i < numPlayers; i++) {
            board.addPlayer(new Player("", armySize[numPlayers-2], i+1)); // creates players for the game
        }
        board.randomizePlayers(); // randomizes player order
        board.buildMap(); // adds all countries to map
        board.placePlayers(numPlayers); // place players randomly on the map
        board.setAdjacentCountries();
        parser.outputCommands();
        board.testConfiguration();

        boolean gameOver = false;
        while (! gameOver) {
            System.out.println("Please input a command");
            Command command = parser.returnCommand();
            gameOver = processCommand(command);
        }
    }

    /**
     * Given a command, process (that is: execute) the command.
     * @param command The command to be processed.
     * @return true If the command ends the game, false otherwise.
     */
    private boolean processCommand(Command command)
    {
        boolean wantToQuit = false;

        if(command.commandEmpty()) {
            System.out.println("I don't know what you mean...");
            return false;
        }

        String commandWord = command.getCommand();
        if (commandWord.equals("attack")) {
            Scanner sc = new Scanner(System.in);

            System.out.println("Here are your countries: ");

            Player player = board.getPlayers().get(turnIndex);
            ArrayList<Country> playerCountries = player.getCountriesOwned();

            for (int i =0; i< playerCountries.size(); i++) {
                System.out.println(i + ": " + playerCountries.get(i).getName());
            }
            System.out.println();


            Integer countryAttackIndex;
            Country attackCountry;

            try{
                countryAttackIndex = sc.nextInt();
                attackCountry = playerCountries.get(countryAttackIndex);

            } catch(InputMismatchException e){ // catch for string input instead of int
                System.out.println("Please enter a number " + e);
                return false;
            } catch(IndexOutOfBoundsException e){ // catch for invalid int
                System.out.println("That value is not in the correct range" + e);
                return false;
            }

            ArrayList<Country> countriesToAttack = attackCountry.getPossibleAttacks();

            if (countriesToAttack.size() == 0){
                System.out.println("This country has no one to attack");
                return false;
            }

            if (attackCountry.getArmySize() == 1){
                System.out.println("This country does not have enough troops to attack");
                return false;
            }

            System.out.println();
            System.out.println("Who would you like to attack? ");

            for(int i = 0; i < countriesToAttack.size(); i++){
                System.out.println(i + ": " + countriesToAttack.get(i).getName());
            }

            // Get Country to be Attacked

            int countryDefendIndex;
            Country defendCountry;
            int numOfAttackers;
            int attackArmySize = attackCountry.getArmySize()-1;

            try{
                countryDefendIndex = sc.nextInt();
                defendCountry = playerCountries.get(countryDefendIndex);
                System.out.println("How many troops to attack with?(1 - " + attackArmySize + ")");
                numOfAttackers = sc.nextInt();


            } catch(InputMismatchException e){ // catch for string input instead of int
                System.out.println("Please enter a number " + e);
                return false;
            } catch(IndexOutOfBoundsException e){ // catch for invalid int
                System.out.println("That value is not in the correct range" + e);
                return false;
            }

            if (!(numOfAttackers >= 1  && numOfAttackers <= attackArmySize)) {
                System.out.println("Attacking with that number of troops is impossible");
                return false;
            }

            attack(attackCountry,defendCountry,numOfAttackers);

        }
        else if (commandWord.equals("go")) {

        }
        else if (commandWord.equals("back")) {

        }
        else if (commandWord.equals("quit")) {

        }

        return wantToQuit;
    }

    public static void main (String[] args) {
        RiskGame game = new RiskGame();
        game.playGame();
    }
}
