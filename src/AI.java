



import javax.swing.*;
import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.*;
import java.util.concurrent.TimeUnit;

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

    public void playTurn(){
        placeTroops(model.bonusTroopCalculation(this));
        attack();
        reinforce();
        endTurn();

    }

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

    public void printProbabilities(){
        for (int i = 0; i < probabilities.length; i++) { //this equals to the row in our matrix.
            for (int j = 0; j < probabilities[i].length; j++) { //this equals to the column in each row.
                System.out.print(probabilities[i][j] + " ");
            }
            System.out.println();
        }
    }



    public void attack(){
        boolean willAttack;
        for (int i =0; i < 3; i++){
            willAttack = false;
            ArrayList<PossibleAIAttack> allPossibleAttacks = getAllPossibleAIAttacks();

            if (allPossibleAttacks.size() == 0) {
                return;
            }


            PossibleAIAttack bestAttack = allPossibleAttacks.get(0);

            for (PossibleAIAttack attack : allPossibleAttacks) {
                if (attack.getProbability() > 0.6) {
                    willAttack = true;
                }

                if (attack.getRelativeScoreIncrease() > bestAttack.getRelativeScoreIncrease()) {
                    bestAttack = attack;
                }

            }

            if (!willAttack) { // will continue to attack until there is not a good attack (>50% win chance)
                return;
            }

            Country attackingCountry = bestAttack.getAttackingCountry();
            Country defendingCountry = bestAttack.getDefendingCountry();

            model.attack(attackingCountry, defendingCountry, attackingCountry.getArmySize() - 1);
        }
    }

    public ArrayList<PossibleAIAttack> getAllPossibleAIAttacks(){

        int currentScore = evaluateGameState(this.copyofCountriesOwned());

        // gets all possible attacks in hashmap form (key= Attacking Country, values=defending Country)
        ArrayList<PossibleAIAttack> allPossibleAttacks = getAllPossibleAttacks();


        for (PossibleAIAttack attack: allPossibleAttacks){
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
                double probabilityOfWinningAttack = probabilities[attackArmy - 2][defendingArmy - 1] / 100;
                attack.setProbability(probabilityOfWinningAttack);

                ArrayList<Country> countriesOwned = this.getCountriesOwned();
                countriesOwned.add(defendingCountry);

                int score = evaluateGameState(countriesOwned);

                int score_increase = score - currentScore;
                int relativeScoreIncrease = (int) (score_increase * probabilityOfWinningAttack);

                attack.setRelativeScoreIncrease(relativeScoreIncrease);
            }
        }
        return allPossibleAttacks;

    }




    public int evaluateGameState(ArrayList<Country> countriesOwned){

        // give points for having a percentage of a continent
        // higher percentage of of continent owned, the greater the points
        // y= x^3
        int score = 0;

        score += gameStateContinents(countriesOwned);

        score += countriesOwned.size();

        return score;
    }

    public double gameStateContinents(ArrayList<Country> countriesOwned){
        Collection<Continent> continents = board.getContinents().values();
        double score = 0;

        for (Continent continent: continents){
            Collection<Country> inContinent = continent.getCountriesCopy();


            inContinent.removeAll(countriesOwned);


            double numOfCountriesInContinent= continent.getCountriesCopy().size();

            double numOfOwnedCountriesInContinent = numOfCountriesInContinent - inContinent.size();
            double percentOwned = numOfOwnedCountriesInContinent / numOfCountriesInContinent;

            // y=x^3
            //x = the percentage of the continent owned
            double y = Math.pow(percentOwned, 3);

            // * 100 is arbitrary
            score += y * 100;

        }
        return score;
    }

    public ArrayList<PossibleAIAttack> getAllPossibleAttacks(){
        ArrayList<PossibleAIAttack> countryAttacks = new ArrayList<>();

        for (Country countryOwned: this.copyofCountriesOwned()){
            for(Country countryToAttack: countryOwned.getPossibleAttacks()){
                countryAttacks.add(new PossibleAIAttack(countryOwned,countryToAttack));
            }
        }
        return countryAttacks;
    }


    public void placeTroops(int troopNum) {
        Random rand = new Random();
        ArrayList<Country> countriesTouchingEnemies = getAllCountriesTouchingEnemies();
        for (int i = 0; i < troopNum; i ++){
            int index = rand.nextInt(countriesTouchingEnemies.size());
            countriesTouchingEnemies.get(index).addArmy();
            model.bonusTroopEvent(countriesTouchingEnemies.get(index), 1);
        }

    }

    public void reinforce(){
        Random rand = new Random();


        for (Country country: this.getCountriesOwned()){

            if (!checkIfCountryTouchesEnemy(country)) { // if country does not touch enemies
                ArrayList<Country> connectedCountries = model.getConnectedCountries(country); // find all allied connected countries
                ArrayList<Country> connectedCountriesTouchingEnemies = getCountriesTouchingEnemies(connectedCountries);

                if (country.getArmySize()>= 2){
                    int index = rand.nextInt(connectedCountriesTouchingEnemies.size());
                    model.reinforce(country,connectedCountriesTouchingEnemies.get(index), country.getArmySize()-1);
                }
            }

        }
    }

    public void endTurn() {
        model.incrementTurnIndex();
        int playerID = board.getPlayers().get(model.getTurnIndex()).getId();
        model.endTurn(playerID);
    }

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



    private boolean checkIfCountryTouchesEnemy(Country country){
        for (Country adjacentCountry: country.getAdjacentCountries()){

            if (!adjacentCountry.getPlayer().equals(country.getPlayer())){
                return true;
            }

        }
        return false;
    }

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



//    public static void main(String[] args) {
//        RiskModel model = new RiskModel();
//        Board board = new Board();
//        HumanPlayer humanPlayer = new HumanPlayer("hi",0,1);
//        AI ai = new AI(model, humanPlayer,board);
//        ai.printProbabilities();
//        System.out.println();
//        System.out.println(ai.probabilities[4-1][2-1]);
//    }
}
