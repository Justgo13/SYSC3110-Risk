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

        return 1;
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
