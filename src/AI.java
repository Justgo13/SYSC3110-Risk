



import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.*;

public class AI {
    private RiskModel model;
    private Player player;
    private Board board;
    private int [][] probabilities;

    public AI(RiskModel model, Player player, Board board){
        this.model=model;
        this.player = player;
        this.board = board;
        this.probabilities = new int[10][10];
        setupProbabilities();

    }

    public void playTurn(){
        placeTroops(model.bonusTroopCalculation(player));
        attack();
        reinforce();
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

        while(true) {

            ArrayList<PossibleAIAttack> allPossibleAttacks = getAllPossibleAIAttacks();

            if (allPossibleAttacks.size() == 0) {
                return;
            }

            boolean willAttack = false;
            PossibleAIAttack bestAttack = allPossibleAttacks.get(0);

            for (PossibleAIAttack attack : allPossibleAttacks) {
                if (attack.getProbability() > 0.5) {
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

        int currentScore = evaluateGameState(player.getCountriesOwned());

        // gets all possible attacks in hashmap form (key= Attacking Country, values=defending Country)
        ArrayList<PossibleAIAttack> allPossibleAttacks = getAllPossibleAttacks();


        for (PossibleAIAttack attack: allPossibleAttacks){
            Country attackingCountry = attack.getAttackingCountry();
            Country defendingCountry = attack.getDefendingCountry();

            // get probability of winning the attack
            double probabilityOfWinningAttack = probabilities[attackingCountry.getArmySize()-2][defendingCountry.getArmySize()-1]/100;
            attack.setProbability(probabilityOfWinningAttack);

            ArrayList<Country> countriesOwned = player.getCountriesOwned();
            countriesOwned.add(defendingCountry);

            int score = evaluateGameState(countriesOwned);

            int score_increase = score - currentScore;
            int relativeScoreIncrease = (int) (score_increase * probabilityOfWinningAttack);

            attack.setRelativeScoreIncrease(relativeScoreIncrease);
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

    public int gameStateContinents(ArrayList<Country> countriesOwned){
        ArrayList<Continent> continents = (ArrayList<Continent>) board.getContinents().values();
        int score = 0;

        for (Continent continent: continents){
            ArrayList<Country> inContinent = continent.getCountries();


            inContinent.removeAll(countriesOwned);


            int numOfCountriesInContinent= continent.getCountries().size();

            int numOfOwnedCountriesInContinent = numOfCountriesInContinent - inContinent.size();
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

        for (Country countryOwned: player.getCountriesOwned()){
            for(Country countryToAttack: countryOwned.getPossibleAttacks()){
                countryAttacks.add(new PossibleAIAttack(countryOwned,countryToAttack));
            }
        }
        return countryAttacks;
    }


    public void placeTroops(int troopNum) {
        Random rand = new Random();
        ArrayList<Country> countriesTouchingEnemies = getCountriesTouchingEnemies();
        for (int i = 0; i < troopNum; i ++){
            int index = rand.nextInt(countriesTouchingEnemies.size());
            countriesTouchingEnemies.get(index).addArmy();
        }
        // TODO When bonus troop update event happens update the troop count
    }

    public void reinforce(){
        Random rand = new Random();
        ArrayList<Country> isolatedCountries = player.getCountriesOwned();
        ArrayList<Country> countriesTouchingEnemies = getCountriesTouchingEnemies();

        isolatedCountries.removeAll(countriesTouchingEnemies); // left with only countries that do not touch enemies

        if (isolatedCountries.size() == 0){
            return;
        }

        for (Country country: isolatedCountries){
            if (country.getArmySize() > 2){
                int index = rand.nextInt(isolatedCountries.size());
                model.reinforce(country, countriesTouchingEnemies.get(index), country.getArmySize()-1);
            }
        }


    }

    private ArrayList<Country> getCountriesTouchingEnemies(){

        ArrayList<Country> countriesTouchingEnemies = new ArrayList<>();
        for (Country country: player.getCountriesOwned()){
            for (Country adjacentCountry: country.getAdjacentCountries()){
                if (adjacentCountry.getPlayer() != player){
                    countriesTouchingEnemies.add(country);
                    break;
                }
            }
        }

        return countriesTouchingEnemies;
    }


    public static void main(String[] args) {
        RiskModel model = new RiskModel();
        Board board = new Board();
        Player player = new Player("hi",0,1);
        AI ai = new AI(model,player,board);
        ai.printProbabilities();
        System.out.println();
        System.out.println(ai.probabilities[4-1][2-1]);
    }
}