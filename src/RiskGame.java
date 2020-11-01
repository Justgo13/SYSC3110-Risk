import java.util.*;

public class RiskGame {
    private Board board;
    private Parser parser;
    private int turnIndex;

    private List<RiskView> views;

    /**
     * Creates an instance of the Risk game
     */
    public RiskGame()
    {
        board = new Board();
        parser = new Parser();
        turnIndex = 0;
        views = new ArrayList<>();
    }

    /**
     * Author: Shashaank
     * @return the hashmap that contains all of the country objects
     */
    public HashMap<String,Country> getCountries(){
        return board.getCountries();
    }

    /**
     * The attack method simulates the risk battle between one attacking country and one defending country and will
     * update the board based on the outcome of the battle
     *
     * @author Jason Gao
     *
     * @param attackingCountry Country that is initializing the battle
     * @param defendingCountry Country that is defending against an attack
     * @param numOfAttackers The number of attackers the attacking country is attacking with
     */

    public void attack(Country attackingCountry, Country defendingCountry, int numOfAttackers) {
        /*
        defending 2+ = 2 die
        defending 1 = 1 die
        attacking n = n die
        highest attacker dice roll competes with highest defender dice
        second highest attacker fights second highest defender
        attacker : 5,5,1 defender : 6,5
           - 6 fight 5 remove attacker
           - 5 fight 5 remove attacker on tie
         */
        Random random = new Random();
        ArrayList<Integer> attackerDice = new ArrayList<>();
        ArrayList<Integer> defenderDice = new ArrayList<>();

        // initialize defender dice values
        if (defendingCountry.getArmySize() >= 2) {
            defenderDice.add(random.nextInt(6)+1);
            defenderDice.add(random.nextInt(6)+1);
        } else if (defendingCountry.getArmySize() == 1) {
            defenderDice.add(random.nextInt(6)+1);
        }

        // initialize attacker dice values
        for (int i = 0; i < numOfAttackers; i++) {
            attackerDice.add(random.nextInt(6)+1);
        }

        // attack phase
        for (int i = defenderDice.size(); i > 0; i--) {
            if (attackerDice.size() == 0) {
                break;
            }
            Integer attackerMax = Collections.max(attackerDice);
            Integer defenderMax = Collections.max(defenderDice);
            System.out.println("Attacker rolled: " + attackerMax);
            System.out.println("Defender rolled: " + defenderMax);
            if (attackerMax > defenderMax) { // attackers win
                defendingCountry.removeArmy();
            } else { // defenders win
                attackingCountry.removeArmy();
                numOfAttackers--;
            }

            // remove the dice from the current attacking phase
            attackerDice.remove(attackerMax);
            defenderDice.remove(defenderMax);
        }

        // defender lost the country
        if (defendingCountry.getArmySize() == 0) {
            System.out.println("Player " + board.getPlayers().get(turnIndex).getId() + ", you have taken " + defendingCountry.getName()
                    + " from Player " + defendingCountry.getPlayer().getId());
            defendingCountry.getPlayer().removeCountry(defendingCountry); // removes the lost country from the defending player
            defendingCountry.setPlayer(attackingCountry.getPlayer()); // assigns country to the dominating player
            board.getPlayers().get(turnIndex).addCountry(defendingCountry);// add new country to the dominating player
            int attackersStayed = attackingCountry.getArmySize() - numOfAttackers;
            defendingCountry.setArmySize(numOfAttackers); // moves remaining attackers to conquered country
            attackingCountry.setArmySize(attackersStayed); // removes attackers from original country
            board.checkEliminated();
        } else {
            System.out.println("You failed to take " + defendingCountry.getName());
        }

        System.out.println("Here is the results of the battle: ");
        System.out.println("Your country troops remaining: " + attackingCountry.getArmySize());
        System.out.println("Defending country troops remaining: " + defendingCountry.getArmySize());
        if(board.checkWinner()){
            System.out.println("Congratulations, "+board.getPlayers().get(0).getName()+". You won!");
            System.exit(0);
        }
        updateAttackView(attackingCountry, defendingCountry);
    }

    /**
     * Initial method that is called to start the risk game
     * @author Jason Gao, Harjap Gill, Shashaank Srivastava, Albaraa Salem
     */
    public void playGame() {
        Scanner sc = new Scanner(System.in);
        System.out.println("Welcome to Risk! Please enter how many players are playing: ");
        int numPlayers = sc.nextInt();

        board.setupBoard(numPlayers);

        //board.testConfiguration();

        boolean gameOver = false;
    }

    public void addRiskView(RiskView view){
        views.add(view);
    }

    /**
     * After an attack has taken place, update the view to reflect the results
     *
     * @param attackingCountry
     * @param defendingCountry
     */
    public void updateAttackView(Country attackingCountry, Country defendingCountry){
        for (RiskView v : views){
            v.handleAttackEvent(new AttackEvent(this,attackingCountry,defendingCountry));
        }
    }

    public Board getBoard() {
        return board;
    }

    public int getTurnIndex() {
        return turnIndex;
    }

    public void incrementTurnIndex() {
        this.turnIndex = (1+turnIndex) % board.getNumOfPlayers();
    }

    public static void main (String[] args) {
        RiskGame game = new RiskGame();
        game.playGame();
    }
}
