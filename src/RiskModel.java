import java.util.*;

public class RiskModel {
    private Board board;
    private int turnIndex;
    private Boolean gameOver;
    private List<RiskView> views;
    private AttackState state;
    private Country attackingCountry;
    private Country defendingCountry;
    private int attackingTroops;
    /**
     * Creates an instance of the Risk game
     */
    public RiskModel()
    {
        board = new Board();
        turnIndex = 0;
        views = new ArrayList<>();
        gameOver = false;
        state = null;
        attackingCountry = null;
        defendingCountry = null;
        attackingTroops = 0;
    }

    /**
     * Initial method that is called to start the risk game
     * @author Jason
     */
    public void playGame(int numPlayers) {
        board.setupBoard(numPlayers);
    }

    /**
     * Adds a view to listen to changes in the model
     * @author Harjap
     * @param view The view instance containing the map and text area console
     */
    public void addRiskView(RiskView view){
        views.add(view);
    }


    /**
     * The attack method simulates the risk battle between one attacking country and one defending country
     * by randomly generating dice values corresponding to how many attacker and defender there are.
     * The board will then be updated by notifying the respective view.
     *
     * @author Jason Gao, Albara'a
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

        // Update the Dice results
        updateDiceResult(attackerDice, defenderDice);

        attackPhase(defendingCountry, attackingCountry, attackerDice, defenderDice, numOfAttackers);

        checkDefenderLostCountry(defendingCountry, attackingCountry, numOfAttackers);

        updateBattleResults(defendingCountry.getArmySize(), attackingCountry.getArmySize());

        if(board.checkWinner()){
            updateGameOver(board.getPlayers().get(0).getId());
            gameOver = true;
            //System.exit(0);
        }
        updateAttackView(attackingCountry, defendingCountry);

    }

    /**
     * Initializes the defending country dice roll through random generation
     * @author Albara'a
     * @param defendingArmySize the size of the defending army
     * @return List of the randomly generated dice rolls
     */
    protected ArrayList<Integer> defendingDiceInitialization(int defendingArmySize){
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
     * Initializes the attacking country dice roll through random generation
     * @author Albara'a
     * @param numOfAttackers the size of the attacking army
     * @return List of the randomly generated dice rolls
     */
    protected  ArrayList<Integer> attackingDiceInitialization(int numOfAttackers){
        Random random = new Random();
        ArrayList<Integer> attackerDice = new ArrayList<>();
        // If number of attackers exceeds 3, set it to 3
        if (numOfAttackers > 3){
            numOfAttackers = 3;
        }

        // initialize attacker dice values
        for (int i = 0; i < numOfAttackers; i++) {
            attackerDice.add(random.nextInt(6)+1);
        }

        return attackerDice;
    }

    /**
     * Performs the attack between the attacking country and defending country and will remove troops from their
     * respective countries based on the result of the dice rolls.
     * @author Albara'a
     * @param defenderDice the defending country's dice rolls
     * @param attackerDice the attacking country's dice rolls
     * @param defendingCountry the country that is defending
     * @param attackingCountry the country that is attacking
     * @param numOfAttackers the size of the attacking army
     */
    protected void attackPhase(Country defendingCountry, Country attackingCountry, ArrayList<Integer> attackerDice, ArrayList<Integer> defenderDice, int numOfAttackers){

        for (int i = defenderDice.size(); i > 0; i--) {
            if (attackerDice.size() == 0) {
                break;
            }
            Integer attackerMax = Collections.max(attackerDice);
            Integer defenderMax = Collections.max(defenderDice);

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
     * Once the defending country is seized by the attacking player, the number of troops remaining in the attack
     * will move into the defending country.
     * @author Albara'a
     * @param defendingCountry the defending country
     * @param attackingCountry the attacking country
     * @param numOfAttackers the size of the attacking army
     */
    protected void checkDefenderLostCountry(Country defendingCountry, Country attackingCountry, int numOfAttackers){
        updateCountryLost(defendingCountry, board.getPlayers().get(turnIndex).getId());
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
     * Returns a boolean stating whether or not a turn can end
     * @return True if a end turn command can be done, false otherwise
     */
    private boolean canEndTurn() {
        return state == AttackState.SHOW_DEFENDING_COUNTRIES || state == null;
    }


    public void countryClicked(Country country) {
        if (state.equals(AttackState.SHOW_DEFENDING_COUNTRIES)) {
            attackingCountry = country;
            for (RiskView v : views) {
                v.handleShowDefendingCountry(country);
            }
            updateNextState();
        } else if (state.equals(AttackState.COMMENCE_ATTACK)) {
            defendingCountry = country;
            attack(attackingCountry, defendingCountry, attackingTroops);
            for (RiskView v : views) {
                v.handleCountryAttack(attackingCountry);
            }
            updatePrevState();
        }
    }

    public void attackClicked() {
        state = AttackState.SHOW_PLAYER_COUNTRIES;
        for (RiskView v : views) {
            v.handleShowAttackingCountry();
        }
        updateNextState();
    }

    /**
     * Notifies the view that an AttackEvent has occurred
     * @author Albara'a
     * @param attackingCountry The attacking country
     * @param defendingCountry The country being attacked
     */
    public void updateAttackView(Country attackingCountry, Country defendingCountry){
        for (RiskView v : views){
            v.handleAttackEvent(new AttackEvent(this,attackingCountry,defendingCountry));
        }
    }

    /**
     * Notifies the view that a battle result event needs to be printed
     * @author Albara'a
     * @param defendingArmySize The defending country army size
     * @param attackingArmySize The attacking coutnry army size
     */
    public void updateBattleResults(int defendingArmySize, int attackingArmySize){
        for(RiskView v : views){
            v.handleResultEvent(new BattleResultEvent(this,attackingArmySize, defendingArmySize));
        }
    }

    /**
     * Notifying the views to handle a dice roll event
     * @author Albara'a
     *
     * @param attackerDice arraylist of the attackers dice rolls
     * @param defenderDice arraylist of the defenders dice rolls
     */
    public void updateDiceResult(ArrayList<Integer> attackerDice, ArrayList<Integer> defenderDice){
        for(RiskView v : views){
            v.handleDiceRolls(new DiceEvent(this,attackerDice,defenderDice));
        }
    }

    /**
     * Notifies the view to handle a country being taken over event
     * @author Albara'a
     * @param defendingCountry The defending country that was taken
     * @param attackingPlayerIndex The player who took the country
     */
    public void updateCountryLost(Country defendingCountry, int attackingPlayerIndex){
        for(RiskView v: views){
            v.handleDefendingCountryLost(new CountryLostEvent(this, defendingCountry, attackingPlayerIndex));
        }

    }

    /**
     * Notifies the view that the game is over and who won
     * @author Jason
     * @param
     */

    public void updateGameOver(int playerID) {
        for (RiskView v : views) {
            v.handleGameOver(playerID);
        }
    }

    /**
     * Notifies the view that a turn has ended
     * @author Albara'a
     */
    public void endTurnPhase(){
        if (canEndTurn()) {
            incrementTurnIndex();
            int playerID = board.getPlayers().get(turnIndex).getId();
            for (RiskView v : views) {
                v.handleEndTurn(playerID);
            }
        }
    }

    public Board getBoard() {
        return board;
    }

    /**
     * Gets the player who ended their turn
     * @author Jason
     * @return An instance of Player corresponding to player who just ended their turn
     */
    public Player getEndTurnPlayer() {
        int playerEndTurn = (((turnIndex-1)%board.getNumOfPlayers()) + board.getNumOfPlayers()) % board.getNumOfPlayers();
        ArrayList<Player> players = this.getBoard().getPlayers();
        return players.get(playerEndTurn);
    }

    /**
     * Gets the current player who is attacking
     * @author Jason
     * @return An instance of Player corresponding to the attacking player
     */
    public Player getAttackingPlayer() {
        ArrayList<Player> players = this.getBoard().getPlayers();
        return players.get(turnIndex);
    }

    public Boolean getGameOver() { return gameOver; }

    public int getTurnIndex() {
        return turnIndex;
    }

    public void incrementTurnIndex() {
        this.turnIndex = (1+turnIndex) % board.getNumOfPlayers();
    }


    public HashMap<String,Country> getCountries(){
        return board.getCountries();
    }

    public void setAttackingTroops(int attackingTroops) {
        this.attackingTroops = attackingTroops;
    }

    public void updateNextState() {
        state = state.next();
    }

    public void updatePrevState() {
        state = state.previous();
    }
}
