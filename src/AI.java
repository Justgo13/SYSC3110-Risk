import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.lang.reflect.Array;
import java.util.*;

public class AI {

    private Player player;
    private Board board;
    private int [][] probabilities;

    public AI(Player player, Board board){
        this.player = player;
        this.board = board;
        this.probabilities = new int[10][10];
        setupProbabilities();

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

    }

    public int evaluateGameState(ArrayList<Country> countriesOwned){

        // give points for having a percentage of a continent
        // higher percentage of of continent owned, the greater the points
        // y= x^4
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

            // y=x^4
            //x = the percentage of the continent owned
            double y = Math.pow(percentOwned, 4);

            score += y * 100;

        }
        return score;
    }


    public void placeTroops(int troopNum) {
        Random rand = new Random();
        ArrayList<Country> countriesTouchingEnemies = getCountriesTouchingEnemies();
        for (int i = 0; i < troopNum; i ++){
            int index = rand.nextInt(countriesTouchingEnemies.size());
            countriesTouchingEnemies.get(index).addArmy();
        }
    }

    public void moveTroops(){
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
                // moveTroops(country, countriesTouchingEnemies.get(index), country.getArmySize()-1);
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
        Board board = new Board();
        Player player = new Player("hi",0,1);
        AI ai = new AI(player,board);
        ai.printProbabilities();
    }
}
