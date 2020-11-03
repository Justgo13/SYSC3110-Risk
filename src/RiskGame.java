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
        ArrayList<Integer> attackerDice = attackingDiceInitialization(numOfAttackers);
        ArrayList<Integer> defenderDice = defendingDiceInitialization(defendingCountry.getArmySize());

        attackPhase(defendingCountry, attackingCountry, attackerDice, defenderDice, numOfAttackers);

        checkDefenderLostCountry(defendingCountry, attackingCountry, numOfAttackers);

        battleResult(defendingCountry.getArmySize(), attackingCountry.getArmySize());

        if(board.checkWinner()){
            System.out.println("Congratulations, "+board.getPlayers().get(0).getName()+". You won!");
            System.exit(0);
        }
        updateAttackView(attackingCountry, defendingCountry);
    }
    /**
     * initializes the defending country dice
     * @param defendingArmySize the size of the defending army
     * @return ArrayList of Integers
     */

    public ArrayList<Integer> defendingDiceInitialization(int defendingArmySize){
        Random random = new Random();
        ArrayList<Integer> defenderDice = new ArrayList<>();
        if (defendingArmySize >= 2) {
            defenderDice.add(random.nextInt(6)+1);
            defenderDice.add(random.nextInt(6)+1);
        } else if (defendingArmySize == 1) {
            defenderDice.add(random.nextInt(6)+1);
        }

        return defenderDice;
    }

    /**
     * initializes the attacking country dice
     * @param numOfAttackers the size of the attacking army
     * @return an ArrayList of Integers
     */
    public  ArrayList<Integer> attackingDiceInitialization(int numOfAttackers){
        Random random = new Random();
        ArrayList<Integer> attackerDice = new ArrayList<>();
        // initialize attacker dice values
        for (int i = 0; i < numOfAttackers; i++) {
            attackerDice.add(random.nextInt(6)+1);
        }

        return attackerDice;
    }

    /**
     * The calculations done after an attack has occured between an attacking
     * and defending country
     * @param defenderDice the defending country's dice arraylist
     * @param attackerDice the attacking country's dice arraylist
     * @param defendingCountry the country that is defending
     * @param attackingCountry the country that is attacking
     * @param numOfAttackers the size of the attacking army
     * @return void
     */
    public void attackPhase(Country defendingCountry, Country attackingCountry, ArrayList<Integer> attackerDice, ArrayList<Integer> defenderDice, int numOfAttackers){
        for (int i = defenderDice.size(); i > 0; i--) {
            if (attackerDice.size() == 0) {
                break;
            }
            Integer attackerMax = Collections.max(attackerDice);
            Integer defenderMax = Collections.max(defenderDice);
            diceResult(attackerMax, defenderMax);
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

    }

    /**
     * Checks if the defending country had lost the battle between the attacking country.
     * If so, calculations are made to the troop numbers
     * @param defendingCountry the defending country
     * @param attackingCountry the attacking country
     * @param numOfAttackers the size of the attacking army
     */
    public void checkDefenderLostCountry(Country defendingCountry, Country attackingCountry, int numOfAttackers){
        defendingCountryLost(defendingCountry, board.getPlayers().get(turnIndex).getId());
        if (defendingCountry.getArmySize() == 0) {
            defendingCountry.getPlayer().removeCountry(defendingCountry); // removes the lost country from the defending player
            defendingCountry.setPlayer(attackingCountry.getPlayer()); // assigns country to the dominating player
            board.getPlayers().get(turnIndex).addCountry(defendingCountry);// add new country to the dominating player
            int attackersStayed = attackingCountry.getArmySize() - numOfAttackers;
            defendingCountry.setArmySize(numOfAttackers); // moves remaining attackers to conquered country
            attackingCountry.setArmySize(attackersStayed); // removes attackers from original country
            board.checkEliminated();
        }

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

    /**
     * Displays the result of the battle between an attacking country and
     * defending country
     * @param defendingArmySize
     * @param attackingArmySize
     */
    public void battleResult(int defendingArmySize, int attackingArmySize){
        for(RiskView v : views){
            v.handleResultEvent(attackingArmySize, defendingArmySize);
        }
    }

    /**
     * Notifying the views to handle dice rolls
     * @param attackerMax
     * @param defenderMax
     */
    public void diceResult(int attackerMax, int defenderMax){
        for(RiskView v : views){
            v.handleDiceRolls(attackerMax, defenderMax);
        }
    }

    public void defendingCountryLost(Country defendingCountry, int attackingPlayerIndex){
        for(RiskView v: views){
            v.handleDefendingCountryLost(defendingCountry, attackingPlayerIndex);
        }

    }

    public void endTurnPhase(int playerID){
        for(RiskView v: views){
            v.handleEndTurn(playerID);
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
}
