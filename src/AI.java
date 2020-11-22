import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.*;

/**
 *This AI class extends the Player class. Rather than a human player making the attacks, an AI will try to make the
 * best attacks, best reinforces and best troop placements.
 *
 */
public class AI extends Player{
    private RiskModel model;
    private Board board;
    private double [][] probabilities;

    public AI(RiskModel model, Board board, int initArmySize, int id){
        super(initArmySize,id);
        this.model=model;
        this.board = board;
        this.probabilities = new double[10][10];
        setupProbabilities();

    }

    /**
     * playTurn method for the AI player to make their turn
     *
     */

    public void playTurn(){
        // The AI will first place troops, then attack and then reinforce
        placeTroops(model.bonusTroopCalculation(this));
        attack();
        reinforce();
        endTurn();

    }

    /**
     * We are importing in dice roll probabilities from a txt file to help the AI make smart decisions. They are stored
     * in a 2d array to be easily accessed in the future
     */

    public void setupProbabilities(){
        try{
            InputStream inputStream = this.getClass().getResourceAsStream("diceProbability.txt");
            BufferedReader br = new BufferedReader(new InputStreamReader(inputStream));
            String line;
            int count = 0;

            while ((line = br.readLine()) != null) {
                String[] l = line.split(" ");

                for (int i = 0; i < 10; i++){
                    this.probabilities[count][i] = Integer.parseInt(l[i]);
                }
                count++;
            }
        }catch(Exception e){
            System.out.println(e);
        }

    }

    /**
     * The attack method to allow the AI to make attacks. The AI uses a utility function and the dice probabilities
     * to estimate which move will result in the best outcome
     */
    public void attack(){
        boolean willAttack;
        // attempts to attack 3 times every turn
        for (int i =0; i < 3; i++){
            willAttack = false;
            // all the possible attacks will be calculated
            ArrayList<PossibleAIAttack> allPossibleAttacks = getAllPossibleAIAttacks();

            if (allPossibleAttacks.size() == 0) { // if no attacks are possible, end attack phase
                return;
            }


            PossibleAIAttack bestAttack = allPossibleAttacks.get(0);

            // iterate over all possible attacks and find the attack that results in the highest utility function output
            for (PossibleAIAttack attack : allPossibleAttacks) {
                if (attack.getProbability() > 0.75) {
                    willAttack = true;
                }

                if (attack.getRelativeScoreIncrease() > bestAttack.getRelativeScoreIncrease()) {
                    bestAttack = attack;
                }

            }

            if (!willAttack) { // will continue to attack until there is not a good attack (>70% win chance)
                return;
            }

            Country attackingCountry = bestAttack.getAttackingCountry();
            Country defendingCountry = bestAttack.getDefendingCountry();
            Country actualAttackingCountry = board.getCountries().get(attackingCountry.getName());
            Country actualDefendingCountry = board.getCountries().get(defendingCountry.getName());
            // tell the Model, to do the best attack
            if (actualAttackingCountry.getArmySize()-1 > 0) {
                model.attack(actualAttackingCountry, actualDefendingCountry, actualAttackingCountry.getArmySize() - 1);
            }
        }
    }

    /**
     * Function to find all possible attacks that can be done.
     *
     * @return An arraylist of all possible attacks
     */
    public ArrayList<PossibleAIAttack> getAllPossibleAIAttacks(){

        // finds the utility score of the AI's current position
        int currentScore = evaluateGameState(this.copyofCountriesOwned());

        // gets all possible attacks
        ArrayList<PossibleAIAttack> allPossibleAttacks = getAllPossibleAttacks();


        for (PossibleAIAttack attack: allPossibleAttacks){ // iterate over all attacks
            Country attackingCountry = attack.getAttackingCountry();
            Country defendingCountry = attack.getDefendingCountry();

            // get probability of winning the attack
            if (attackingCountry.getArmySize() > 2) {
                int attackArmy = attackingCountry.getArmySize();
                int defendingArmy = defendingCountry.getArmySize();
                if (attackArmy >= 10) {
                    attackArmy = 10;
                }
                if (defendingArmy >= 10) {
                    defendingArmy = 10;
                }
                // for each attack possibility, determines the probability of winning
                double probabilityOfWinningAttack = probabilities[attackArmy - 2][defendingArmy - 1] / 100;
                attack.setProbability(probabilityOfWinningAttack);

                ArrayList<Country> countriesOwned = this.copyofCountriesOwned();
                countriesOwned.add(defendingCountry);

                int score = evaluateGameState(countriesOwned);

                // find the increase in utility score expected by doing this attack
                int score_increase = score - currentScore;
                int relativeScoreIncrease = (int) (score_increase * probabilityOfWinningAttack);

                // add this expected utility score increase to the PossibleAIAttack object
                attack.setRelativeScoreIncrease(relativeScoreIncrease);
            }
        }
        return allPossibleAttacks;

    }


    /**
     * Takes a list of countries that may be owned and determines the utility score of owning those countries
     *
     * @param countriesOwned ArrayList of countries that could be owned
     * @return integer utility score evaluating how good those countries are
     */
    public int evaluateGameState(ArrayList<Country> countriesOwned){

        // give points for having a percentage of a continent
        // higher percentage of of continent owned, the greater the points
        // y= x^3
        int score = 0;

        // increase the score based on continents owned
        score += gameStateContinents(countriesOwned);

        // increase the score based on countries owned
        score += countriesOwned.size();

        return score;
    }

    /**
     * Given a list of countries, determines how valuable it's continents owned are
     *
     * @param countriesOwned list of countries that could be owned
     * @return the double of how much to increase utility score
     */
    public double gameStateContinents(ArrayList<Country> countriesOwned){
        // finds all the continents in the game
        Collection<Continent> continents = board.getContinents().values();
        double score = 0;


        for (Continent continent: continents){
            Collection<Country> inContinent = continent.getCountriesCopy();

            int numberCountriesOwnedInContinent = 0;
            for (Country c : inContinent) {
                for (Country c1 : countriesOwned) {
                    if (c.getName().equals(c1.getName())) {
                        numberCountriesOwnedInContinent++;
                        break;// find how many countries of a continent the player owns
                    }
                }
            }


            double numOfCountriesInContinent= continent.getCountriesCopy().size();

            // find the percentage (0 to 1) of a continent the player owns
            double percentOwned = numberCountriesOwnedInContinent / numOfCountriesInContinent;

            // Curves the percentage of continent owned to incentivize owning a higher percentage of a continent
            // curve is set to y=x^3
            double y = Math.pow(percentOwned, 3);

            // * 100 is arbitrary
            score += y * 100;

        }
        return score;
    }

    /**
     * Find all possible attacks that can be done
     *
     * @return an arraylist of all possible attacks
     */
    public ArrayList<PossibleAIAttack> getAllPossibleAttacks(){
        ArrayList<PossibleAIAttack> countryAttacks = new ArrayList<>();

        for (Country countryOwned: this.copyofCountriesOwned()){
            for(Country countryToAttack: countryOwned.getPossibleAttacks()){
                countryAttacks.add(new PossibleAIAttack(countryOwned,countryToAttack));
            }
        }
        return countryAttacks;
    }

    /**
     * Places troops on countries that are touching enemy countries
     *
     * @param troopNum Number of troops to place
     */
    public void placeTroops(int troopNum) {
        Random rand = new Random();
        ArrayList<Country> countriesTouchingEnemies = getAllCountriesTouchingEnemies();
        for (int i = 0; i < troopNum; i ++){
            int index = rand.nextInt(countriesTouchingEnemies.size());
            countriesTouchingEnemies.get(index).addArmy();
            model.bonusTroopEvent(countriesTouchingEnemies.get(index), 1);
        }

    }

    /**
     * After attacking, moves troops around if necessary. If a country has more than 1 troop and it is not touching
     * any enemy countries, it will move those troops to countries touching enemies
     *
     */
    public void reinforce(){
        Random rand = new Random();


        for (Country country: this.getCountriesOwned()){

            if (!checkIfCountryTouchesEnemy(country)) { // if country does not touch enemies
                ArrayList<Country> connectedCountries = model.getConnectedCountries(country); // find all allied connected countries
                ArrayList<Country> connectedCountriesTouchingEnemies = getCountriesTouchingEnemies(connectedCountries);

                if (country.getArmySize()>= 2){
                    int index = rand.nextInt(connectedCountriesTouchingEnemies.size());
                    // adds troop to random country touching enemy
                    model.reinforce(country,connectedCountriesTouchingEnemies.get(index), country.getArmySize()-1);
                }
            }

        }
    }

    /**
     * End AI player's turn
     */
    public void endTurn() {
        model.incrementTurnIndex();
        int playerID = board.getPlayers().get(model.getTurnIndex()).getId();
        model.endTurn(playerID);
    }

    /**
     * Find all countries player owns that touch enemy countries
     *
     * @return Arraylist of countries that touch enemy countries
     */
    private ArrayList<Country> getAllCountriesTouchingEnemies(){

        ArrayList<Country> countriesTouchingEnemies = new ArrayList<>();
        for (Country country: this.getCountriesOwned()){
            for (Country adjacentCountry: country.getAdjacentCountries()){
                if (adjacentCountry.getPlayer() != this){
                    countriesTouchingEnemies.add(country);
                }
            }
        }

        return countriesTouchingEnemies;
    }


    /**
     * Check if specified country is touching a enemy country
     *
     * @param country Country to check if touches enemy
     * @return boolean to say true if touches enemy, false otherwise
     */
    private boolean checkIfCountryTouchesEnemy(Country country){
        for (Country adjacentCountry: country.getAdjacentCountries()){

            if (!adjacentCountry.getPlayer().equals(country.getPlayer())){
                return true;
            }

        }
        return false;
    }

    /**
     * Get Countries touching enemies with country list passed in
     *
     * @param countries Countries to check which ones touch enemies
     * @return arraylist of countries that are touching enemies
     */
    private ArrayList<Country> getCountriesTouchingEnemies(ArrayList<Country> countries){
        ArrayList<Country> countriesTouchingEnemies = new ArrayList<>();
        for (Country country: countries){
            for (Country adjacentCountry: country.getAdjacentCountries()){
                if (!adjacentCountry.getPlayer().equals(country.getPlayer())){
                    countriesTouchingEnemies.add(country);
                    break;
                }
            }
        }
        return countriesTouchingEnemies;
    }
}
