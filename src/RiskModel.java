import org.json.simple.JSONArray;
import org.json.simple.JSONObject;

import java.io.*;
import java.util.*;

public class RiskModel implements Serializable {
    private static final String JSON_COUNTRIES_KEY = "Countries";
    private static final String JSON_CONTINENT_KEY = "Continents";
    private static final int ONE_ARMY = 1;
    private static final int TWO_ARMIES = 2;
    private static final int THREE_ATTACKERS = 3;
    private static final int INVALID_PLAYER_ID = 0;
    private static final int INITIAL_TROOP_BONUS = 3;
    private static final int DICE_VALUES = 6;
    private static final int DEFAULT_MULTIPLE_ATTACKER = 3;
    private static final int BONUS_TROOP_DIVIDEND = 3;
    private static final String JSON_COUNTRY_NAME =  "countryName";
    private static final String ADJACENT_COUNTRY = "adjacentCountries";
    private Board board;
    private int turnIndex;
    private Boolean gameOver;
    private List<RiskView> views;
    private GameState state;
    private EndPhase endPhaseState;
    private Country attackingCountry;
    private Country defendingCountry;
    private Country reinforceCountry;
    private Country countryToReinforce;
    private Country bonusCountry;
    private int movedTroops;
    private List<Player> playersList;
    private JSONObject jsonObject;
    /**
     * Creates an instance of the Risk game
     */
    public RiskModel() {
        board = new Board();
        turnIndex = 0;
        views = new ArrayList<>();
        gameOver = false;
        state = null;
        endPhaseState = null;
        attackingCountry = null;
        defendingCountry = null;
        reinforceCountry = null;
        countryToReinforce = null;
        bonusCountry = null;
        movedTroops = 0;
        playersList = new ArrayList<>();
        jsonObject = null;
    }

    /**
     * Initial method that is called to start the risk game
     * @author Jason
     */
    public void playGame(int numHumanPlayers, int aiPlayers) {
        int totalPlayers = numHumanPlayers + aiPlayers;
        for(int i = 0; i< numHumanPlayers; i++) {
            this.playersList.add(new HumanPlayer(returnArmySize(totalPlayers), i+1));
        }
        for (int i = numHumanPlayers; i < totalPlayers; i++) {
            this.playersList.add(new AI(this, board,returnArmySize(totalPlayers) ,i+1));
        }
        JSONArray countries = (JSONArray) jsonObject.get(JSON_COUNTRIES_KEY);
        JSONArray continents = (JSONArray) jsonObject.get(JSON_CONTINENT_KEY);
        board.setupBoard(numHumanPlayers, this.playersList, countries, continents);
    }

    /**
     * Takes in a JSONObject representing the map we are loading into the game. This method ensure that this is a
     * valid map to play on and that there are no countries that are disconnected from other
     * @author Harjap
     * @param jsonMap JSONObject representing the map to be loaded
     * @return boolean true if map is valid, and false otherwise
     */
    public boolean validateJSONMap(JSONObject jsonMap){

        HashMap<String,Country> countries = countriesFromJSON(jsonMap);

        // get number of countries in the JSON map
        Collection<Country> allCountries = countries.values();
        int numOfCountries = allCountries.size();

        // Get number of countries connected to an arbitrary country
        ArrayList<Country> randomCountries = new ArrayList<>(countries.values());
        Country randomCountry = randomCountries.get(0);
        ArrayList<Country> connectedCountries = getConnectedCountries(randomCountry);

        // compare number of countries to number of connected countries
        if (numOfCountries == connectedCountries.size()){
            return true;
        }
        return false;
    }

    /**
     * Method that takes a JSONObject of the country map. It will then construct all of the countries within the map
     * and return it in a hashmap
     * @author Harjap
     * @param jsonMap JSONObject representing the country map
     * @return Hashmap<String, Country> that holds all of the countries in the game
     */
    public HashMap<String,Country> countriesFromJSON(JSONObject jsonMap){

        JSONArray countriesJSON = (JSONArray) jsonMap.get(JSON_COUNTRIES_KEY);
        Iterator iterator = countriesJSON.iterator();

        JSONObject jsonCountry;
        HashMap<String,Country> countries = new HashMap<>();

        // adding all the countries
        while(iterator.hasNext()){
            jsonCountry = (JSONObject) iterator.next();
            String countryName = (String) jsonCountry.get(JSON_COUNTRY_NAME);
            countries.put(countryName, new Country(countryName));
        }

        iterator = countriesJSON.iterator();
        JSONArray adjacentCountries;

        // add adjacent countries
        while(iterator.hasNext()){
            jsonCountry = (JSONObject) iterator.next();
            String countryName = (String) jsonCountry.get(JSON_COUNTRY_NAME);
            adjacentCountries = (JSONArray) jsonCountry.get(ADJACENT_COUNTRY);

            for (Object adjCountryName: adjacentCountries){
                String name = adjCountryName.toString();
                Country adjacentCountry = countries.get(name);
                Country country = countries.get(countryName);
                country.addAdjacentCountry(adjacentCountry);
            }
        }
        return countries;
    }

    public void saveGame(){
        File file = new File("Serialize.txt");
        try(FileOutputStream fos = new FileOutputStream(file)){
            if(!file.exists()) file.createNewFile();
            ObjectOutputStream out = new ObjectOutputStream(fos);
            out.writeObject(this);
            out.close();
        } catch (IOException e) {
            e.printStackTrace();
        }

    }


    private void writeObject( ObjectOutputStream out ) throws IOException {
        out.write( this );
        out.flush();
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

        updateBattleResults(attackingCountry.getPlayer(), defendingCountry.getArmySize(), attackingCountry.getArmySize());

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
        if (defendingArmySize >= TWO_ARMIES) {
            defenderDice.add(random.nextInt(DICE_VALUES)+1);
            defenderDice.add(random.nextInt(DICE_VALUES)+1);
        } else if (defendingArmySize == ONE_ARMY) {
            defenderDice.add(random.nextInt(DICE_VALUES)+1);
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
        if (numOfAttackers > THREE_ATTACKERS){
            numOfAttackers = DEFAULT_MULTIPLE_ATTACKER;
        }

        // initialize attacker dice values
        for (int i = 0; i < numOfAttackers; i++) {
            attackerDice.add(random.nextInt(DICE_VALUES)+1);
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
        updateCountryLost(attackingCountry, defendingCountry, board.getPlayers().get(turnIndex).getId());
        if (defendingCountry.getArmySize() == 0) {
            defendingCountry.getPlayer().removeCountry(defendingCountry); // removes the lost country from the defending player
            defendingCountry.setPlayer(attackingCountry.getPlayer()); // assigns country to the dominating player
            board.getPlayers().get(turnIndex).addCountry(defendingCountry);// add new country to the dominating player
            int attackersStayed = attackingCountry.getArmySize() - numOfAttackers;
            defendingCountry.setArmySize(numOfAttackers); // moves remaining attackers to conquered country
            attackingCountry.setArmySize(attackersStayed); // removes attackers from original country
            int playerID = board.checkEliminated();
            if (playerID > INVALID_PLAYER_ID) {
                updatePlayerEliminated(playerID);
            }
        }
    }

    /**
     * Get all the countries you can reinforce with
     * @author Jason
     * @return List of countries that the player can reinforce with
     */
    public ArrayList<Country> getReinforceCountries() {
        Player player = board.getPlayers().get(turnIndex);
        ArrayList<Country> reinforceCountry = new ArrayList<>();
        for (Country c : player.getCountriesOwned()) {
            for (Country adj : c.getAdjacentCountries()) {
                if (adj.getPlayer().equals(player) && !reinforceCountry.contains(c)) {
                    reinforceCountry.add(c);
                    break;
                }
            }
        }
        return reinforceCountry;
    }

    /**
     * Gets a list of countries linked to the passed in country as an adjacency path
     * @author Shashaank
     * @param country The country reinforcing from
     * @return A list of all countries the passed in country can reinforce
     */
    public ArrayList<Country> getConnectedCountries(Country country){
        ArrayList<Country> adjacentCountries = new ArrayList();
        countryRecurse(adjacentCountries, country);
        return adjacentCountries;

    }

    /**
     * Recursively looks through the player's countries and build an adjacency path to the passed in country
     * @author Shashaank
     * @param playerCountries A list of the player's countries
     * @param country The country that we reinforce from
     */
    public void countryRecurse(ArrayList playerCountries, Country country){
        playerCountries.add(country);
        for (Country adjacent: country.getAdjacentCountries()){
            //if owned by same player and not in list already add it(get 0 is the first country that was passed into the function)
            if(adjacent.getPlayer() == ((Country) playerCountries.get(0)).getPlayer() && !playerCountries.contains(adjacent)) {
                countryRecurse(playerCountries, adjacent);
            }
        }
    }

    /**
     * Reinforces the designated country with specific troops and updates the view
     * @author Jason
     * @param reinforceCountry The country to reinforce from
     * @param countryToReinforce The country to reinforce
     * @param reinforceTroops The number of troops being moved
     */
    public void reinforce(Country reinforceCountry, Country countryToReinforce, int reinforceTroops) {
        reinforceCountry.setArmySize(reinforceCountry.getArmySize() - reinforceTroops);
        countryToReinforce.setArmySize(countryToReinforce.getArmySize() + reinforceTroops);
        updateMoveResult(reinforceCountry, countryToReinforce, reinforceTroops);
        updateReinforceView(reinforceCountry, countryToReinforce);
    }

    /**
     * A handler to delegate button clicks from the controller and invoke the correct view methods
     * based on the current state of the game. The state is then updated after the view is updated.
     * @param country The country that was clicked
     */
    public void countryClicked(Country country) {
        if (state.equals(GameState.SHOW_DEFENDING_COUNTRIES)) {
            attackingCountry = country;
            for (RiskView v : views) {
                v.handleShowDefendingCountry(country);
            }
            phaseCancelHandler(state,country);
        } else if (state.equals(GameState.COMMENCE_ATTACK)) {
            defendingCountry = country;
            attack(attackingCountry, defendingCountry, movedTroops);
            movedTroops = 0;
            for (RiskView v : views) {
                v.handleCountryAttack(attackingCountry);
            }
        } else if (state.equals(GameState.CHOOSE_REINFORCE)) {
            reinforceCountry = country;
            for (RiskView v : views) {
                v.handleShowReinforceAdjacents(country);
            }
            phaseCancelHandler(state,country);
        } else if (state.equals(GameState.COMMENCE_REINFORCE)) {
            countryToReinforce = country;
            reinforce(reinforceCountry, countryToReinforce, movedTroops);
            movedTroops = 0;
            for (RiskView v : views) {
                v.handleReinforce(reinforceCountry);
            }
        } else if (state.equals(GameState.CHOOSE_BONUS)){
            bonusCountry = country;
            bonusTroopEvent(bonusCountry, bonusTroopCalculation(bonusCountry.getPlayer()) - movedTroops);
            troopsFinishedHandler();
        }
    }

    /**
     * Handler for when bonus troops have been placed
     * @author Shashaank
     */
    public void troopsFinishedHandler(){
        if(movedTroops == bonusTroopCalculation(bonusCountry.getPlayer())) {
            updateNextState();
            //Making sure the bonus troops placed so far for the next player are 0
            movedTroops = 0;
            for (RiskView v : views) {
                v.troopBonusComplete();
            }

        }
    }

    /**
     * Handler for phase cancels in attack and reinforce
     * @author Jason
     * @param state The state of the game represented by a GameState enum
     * @param country The country that was clicked
     */
    public void phaseCancelHandler(GameState state,Country country){
        if (movedTroops == 0 && state.equals(GameState.CHOOSE_REINFORCE)) {
            for (RiskView v : views) {
                v.handleReinforceCancelled(country);
            }
            updatePrevState();
        }else if(movedTroops == 0 && state.equals(GameState.SHOW_DEFENDING_COUNTRIES)){
            for (RiskView v : views) {
                v.handleAttackCancelled(country);
            }
            updatePrevState();
        }
        else {
            updateNextState();
        }
    }

    /**
     * Handles the bonus troop placement event
     * @author Shashaank
     * @param bonusCountry The country to add troops to
     * @param bonusTroopsPlaced The number of bonus troops
     */
    public void bonusTroopEvent(Country bonusCountry, int bonusTroopsPlaced) {
        for (RiskView v : views) {
            v.handleTroopPlaced(new BonusTroopEvent(this, bonusCountry, bonusTroopsPlaced));
        }
    }

    /**
     * Handles the place troops button being clicked and updates the view accordingly
     * @author Shashaank
     */
    public void placeTroopsClicked() {
        state = GameState.BONUS_PHASE;
        endPhaseState = EndPhase.BONUS_PHASE;
        for (RiskView v : views) {
            v.handleShowTroopPlacementCountry();
        }
        updateNextState();
    }

    /**
     * Handles the attack button being clicked and updates the view accordingly
     * @author Jason
     */
    public void attackClicked() {
        state = GameState.SHOW_PLAYER_COUNTRIES;
        endPhaseState = EndPhase.ATTACK_PHASE;
        for (RiskView v : views) {
            v.handleShowAttackingCountry();
        }
        updateNextState();
    }

    /**
     * Handles the reinforce button being clicked and updates the view accordingly
     * @author Jason
     */
    public void reinforceClicked() {
        state = GameState.SHOW_REINFORCE_COUNTRIES;
        endPhaseState = EndPhase.REINFORCE_PHASE;
        for (RiskView v : views) {
            v.handleShowReinforceCountry();
        }
        updateNextState();
    }

    /**
     * Handles the end phase being clicked and based on the current end phase state,
     * it will handle the view accordingly
     * @author Jason
     */
    public void endPhaseClicked(){
        updateEndPhaseState();
        if (endPhaseState.equals(EndPhase.ATTACK_PHASE)) {
            int playerID = board.getPlayers().get(turnIndex).getId();
            for (RiskView v : views) {
                v.handleEndBonus(playerID);
            }
        } else if (endPhaseState.equals(EndPhase.REINFORCE_PHASE)) {
            int playerID = board.getPlayers().get(turnIndex).getId();
            for (RiskView v : views) {
                v.handleEndAttack(playerID);
            }
        } else if (endPhaseState.equals(EndPhase.END_PHASE)) {
            incrementTurnIndex();
            int playerID = board.getPlayers().get(turnIndex).getId();
            endTurn(playerID);
        }
    }

    /**
     * Handles the end turn event and updates the view accordingly
     * @author Jason
     */
    public void endTurn(int playerID) {
        for (RiskView v : views) {
            v.handleEndTurn(playerID);
        }
    }

    /**
     * According how many countries or continents the player owns,
     * they will receive a specific troop bonus at the beginning
     * of their turn with a minimum of 3.
     * @author Albara'a
     * @param player The player who is getting bonus troops
     * @return int number of bonus troops
     */
    public int bonusTroopCalculation(Player player){
        int troops = INITIAL_TROOP_BONUS;
        troops += player.getCountriesOwned().size() / BONUS_TROOP_DIVIDEND;

        for(Continent c: board.getContinents().values()){
            if(player.equals(c.getContinentOwner())){
                troops += c.getBonusArmy();
            }
        }
        return troops;
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
     * @param player The current player
     * @param defendingArmySize The defending country army size
     * @param attackingArmySize The attacking coutnry army size
     */
    public void updateBattleResults(Player player, int defendingArmySize, int attackingArmySize){
        for(RiskView v : views){
            v.handleResultEvent(new BattleResultEvent(this, player, attackingArmySize, defendingArmySize));
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
     * @param attackingCountry
     * @param defendingCountry The defending country that was taken
     * @param attackingPlayerIndex The player who took the country
     */
    public void updateCountryLost(Country attackingCountry, Country defendingCountry, int attackingPlayerIndex){
        for(RiskView v: views){
            v.handleDefendingCountryLost(new CountryLostEvent(this, attackingCountry, defendingCountry, attackingPlayerIndex));
        }

    }

    /**
     * Notifies the view that the game is over and who won
     * @author Jason
     * @param playerID The id of the winning player
     */
    public void updateGameOver(int playerID) {
        for (RiskView v : views) {
            v.handleGameOver(playerID);
        }
    }

    /**
     * Handles the event when a player is eliminated
     * @author Harjap
     * @param playerID The player ID of the player who was eliminated
     */
    public void updatePlayerEliminated(int playerID) {
        for (RiskView v : views) {
            v.handlePlayerEliminated(playerID);
        }
    }

    /**
     * Handles the reinforce result by updating the text area accordingly
     * @author Jason
     * @param reinforceCountry The country to reinforce from
     * @param countryToReinforce The country being reinforced
     * @param countryToReinforceArmy The reinforcing troops
     */
    private void updateMoveResult(Country reinforceCountry, Country countryToReinforce, int countryToReinforceArmy) {
        for(RiskView v : views){
            v.handleReinforceResultEvent(new ReinforceResultEvent(this,reinforceCountry,countryToReinforceArmy, countryToReinforce));
        }
    }

    /**
     * Handles the reinforce event by updating the view accordingly
     * @author Jason
     * @param reinforceCountry The country to reinforce from
     * @param countryToReinforce The country being reinforced
     */
    private void updateReinforceView(Country reinforceCountry, Country countryToReinforce) {
        for (RiskView v : views){
            v.handleReinforceEvent(new ReinforceEvent(this,reinforceCountry,countryToReinforce));
        }
    }

    /**
     * Gets the army size based on player count
     * @author Albara'a
     * @param totalNumPlayers The number of players
     * @return StartGameTroops constant indicating how many initial troops to use
     */
    public int returnArmySize(int totalNumPlayers){
        switch(totalNumPlayers){
            case 2:
                return StartGameTroops.TWO_PLAYER_GAME.returnValues();
            case 3:
                return StartGameTroops.THREE_PLAYER_GAME.returnValues();
            case 4:
                return StartGameTroops.FOUR_PLAYER_GAME.returnValues();
            case 5:
                return StartGameTroops.FIVE_PLAYER_GAME.returnValues();
            case 6:
                return StartGameTroops.SIX_PLAYER_GAME.returnValues();
        }
        return -1;
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

    public void setMovedTroops(int movedTroops) {
        this.movedTroops = movedTroops;
    }

    public void updateNextState() {
        state = state.next();
    }

    public void updatePrevState() {
        state = state.prev();
    }

    public void updateEndPhaseState() {
        endPhaseState = endPhaseState.next();
    }

    public void setJsonObject(JSONObject jsonObject) {
        this.jsonObject = jsonObject;
    }
}
