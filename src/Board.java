import java.io.*;
import java.util.*;

public class Board {
    private int numOfPlayers;
    private static final int[] armySize = {50,35,30,25,20};
    private ArrayList<Player> players;
    private HashMap<String, Country> countries;
    private HashMap<String, Continent> continents;
    private static final int COUNTRY_COUNT = 42;
    private ArrayList<String> countryNames;
    private ArrayList<String> continentNames;


    private  ArrayList<ArrayList<String>> continentCountries;
    private  ArrayList<Integer> continentBonusArmies;

    /**
    * @author Jason, Harjap, Shashaank
    * Initializes data structures such as HashMaps and ArrayLists to store countries, players, and continents
    */
    public Board() {
        numOfPlayers = 0; // will be changed at runtime
        players = new ArrayList<>();
        countries = new HashMap<>();
        continents = new HashMap<>();
        continentCountries = new ArrayList<>();
        countryNames = new ArrayList<>();

        continentBonusArmies = new ArrayList<>(Arrays.asList(5,2,5,3,7,2));

        continentNames = new ArrayList<>();
        //added buildMap so that countries map will be populated when used (testing for countrybutton)
    }

    public HashMap<String, Country> getCountries(){
        return countries;
    }

    public HashMap<String, Continent> getContinents() {
        return continents;
    }

    public void addPlayer(Player player) {
        players.add(player);
    }

    public ArrayList<Player> getPlayers(){return players;}

    public int getNumOfPlayers() {
        return numOfPlayers;
    }

    public void setNumOfPlayers(int numOfPlayers) {
        this.numOfPlayers = numOfPlayers;
    }

    public void randomizePlayers () {
        Collections.shuffle(players);
    }

    /**
     * Checks to see if any players have been eliminated and removes them from the player pool
     *
    * @author Shashaank
    *
    */
    public int checkEliminated(){
        for (Player player : players){
            if (player.getCountriesOwned().isEmpty()){
                int playerID = player.getId();
                players.remove(player);
                return playerID;
                //only 1 player can be removed for every call of attack()
            }
        }
        return -1;
    }

    /**
     * Method called during configuration to set up the board
     *
     * @author Harjap Gill
     * @param numPlayers number of players in the game
     * @param aiPlayers
     */
    public void setupBoard(int numPlayers, List<AI> aiPlayers){
        setNumOfPlayers(numPlayers+aiPlayers.size());
        int totalPlayers = numPlayers + aiPlayers.size();
        for (int i = 0; i < numPlayers; i++) {
            addPlayer(new HumanPlayer(armySize[totalPlayers-2], i+1)); // creates players for the game
        }
        randomizePlayers(); // randomizes player order
        for (int i = numPlayers; i < totalPlayers; i++) {
            aiPlayers.get(i-numPlayers).setInitArmySize(armySize[totalPlayers-2]);
            aiPlayers.get(i-numPlayers).setId(i+1);
            addPlayer(aiPlayers.get(i-numPlayers));
        }
        buildContinentNames();
        buildCountryNames();
        buildContinent();
        buildMap(); // adds all countries to map
        placePlayers(totalPlayers); // place players randomly on the map
        setAdjacentCountries();
    }

    /**
     * builds the maps by populating the Countries and continents
     * @author Shashaank
     *
     *
     */
    private void buildMap() {
        for (String countryName : countryNames) {
            countries.put(countryName, new Country(countryName));
        }
        for (int i = 0; i<continentNames.size(); i++) {
            continents.put(continentNames.get(i), new Continent(continentNames.get(i), continentBonusArmies.get(i))); // creates continents objects

            //gets all continents and populates them with their specific countries
            //also gives every country the continent it belongs in
            for(int j = 0; j<continentCountries.get(i).size(); j++){
                Continent continent = continents.get(continentNames.get(i));
                continent.addCountry((String) continentCountries.get(i).get(j),countries.get(continentCountries.get(i).get(j)) ); // populates continents with their specific countries
                countries.get(continentCountries.get(i).get(j)).setContinent(continent.getName()); // sets the continent name of each country
            }
        }
    }

    /**
     * Place players on countries based on the number of players. Distribute troops to the various countries
     * randomly ensuring every country has at least one troop. Places extra troops for each player after the initial
     * troop placement is over.
     *
     * @author Jason Gao
     * @param numPlayers number of players in the game
     */
    private void placePlayers(int numPlayers) {
        int countryIndex;
        final int TROOPS = players.size() * players.get(0).getInitArmySize();
        int playerCountriesHeld = Math.floorDiv(COUNTRY_COUNT, numPlayers); // evenly divides total regions based on player count
        int troopAssignedCount = 0;
        int extraCountries = COUNTRY_COUNT % numPlayers; // accounts for extra countries
        Random random = new Random();
        ArrayList<String> tempCountries = countryNames;
        // ------------------------------------------
        // Add players to countries evenly and give the country the player object
        while (tempCountries.size() > extraCountries) {
            for (Player player : players) {
                for (int i = 0; i < playerCountriesHeld; i++) {
                    countryIndex = random.nextInt(tempCountries.size());
                    player.addCountry(countries.get(countryNames.get(countryIndex))); // adds a randomly selected country to the player
                    countries.get(countryNames.get(countryIndex)).setPlayer(player); // assign a player to a country
                    countries.get(countryNames.get(countryIndex)).setArmySize(1); // assign one troop to each player country
                    troopAssignedCount++;
                    tempCountries.remove(countryIndex);
                }
            }
        }
        // Adds extra countries to players and player to respective countries
        for (int i = 0; i<extraCountries; i++) {
            countryIndex = random.nextInt(tempCountries.size());
            int randomPlayer = random.nextInt(players.size());
            players.get(randomPlayer).addCountry(countries.get(countryNames.get(countryIndex)));
            countries.get(countryNames.get(countryIndex)).setPlayer(players.get(randomPlayer));
            countries.get(countryNames.get(countryIndex)).setArmySize(1);
            troopAssignedCount++;
            tempCountries.remove(countryIndex);
        }
        // --------------------------------------

        // remaining troops to place after placing one in each of the 42 countries
        while (troopAssignedCount < TROOPS) {
            for (Player player : players) {
                if (troopAssignedCount >= TROOPS)
                    break;
                countryIndex = random.nextInt(player.getCountriesOwned().size()); // get random country owned by player
                Country country = player.getCountriesOwned().get(countryIndex); // gets the Country object
                country.setArmySize(country.getArmySize()+1); // adds 1 to the armySize of the Country
                troopAssignedCount++;
            }
        }
    }

    private void buildContinent() {
        try {
            InputStream inputStream = this.getClass().getResourceAsStream("continentCountries.txt");
            // buffered reader to read the file
            BufferedReader br = new BufferedReader(new InputStreamReader(inputStream));

            String line;
            while ((line = br.readLine()) != null) {

                ArrayList<String> continentCountry = new ArrayList<>();
                String[] countries = line.split(", ");
                for (int i = 0; i < countries.length; i++) {
                    continentCountry.add(countries[i]);
                }
                continentCountries.add(continentCountry);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void buildCountryNames() {
        try {
            InputStream inputStream = this.getClass().getResourceAsStream("countryNames.txt");
            // buffered reader to read the file
            BufferedReader br = new BufferedReader(new InputStreamReader(inputStream));

            String line;
            while ((line = br.readLine()) != null) {
                countryNames.add(line);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void buildContinentNames() {
        try {
            InputStream inputStream = this.getClass().getResourceAsStream("continentNames.txt");
            // buffered reader to read the file
            BufferedReader br = new BufferedReader(new InputStreamReader(inputStream));

            String line;
            while ((line = br.readLine()) != null) {
                continentNames.add(line);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

        /**
     * For every country in the game, read a .txt file to determine all of the adjacent countries to it. Add all
     * adjacent countries to the corresponding country
     *
     * @author Harjap Gill
     */
    private void setAdjacentCountries(){

        try {
            InputStream inputStream = this.getClass().getResourceAsStream("adjacentCountries.txt");
            // buffered reader to read the file
            BufferedReader br = new BufferedReader(new InputStreamReader(inputStream));

            String line;
            // iterate through every line of the file
            while ((line = br.readLine()) != null) {
                ArrayList<String>  countryLine = new ArrayList<>();// for each line, creat an empty arraylist

                for (String country: line.split(", ")){ // add every country in line to arraylist
                    countryLine.add(country);

                }
                //get country on which we will be setting the adjacents, the first one in line
                Country intialCountry = countries.get(countryLine.get(0));

                for (int i =1; i<countryLine.size(); i++){ // iterate through every country in line other than first

                    Country adjacentCountry = countries.get(countryLine.get(i)); // initialize the adjacent country
                    intialCountry.addAdjacentCountry(adjacentCountry);  // add that adjacent country to initial country's adjacent
                }

            }

        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }


    }

    /**
     * Checks if a player has won the game
     * @author Albara'a
     * @return true if a player has won, false otherwise
     */
    public boolean checkWinner(){
        return (players.size()==1);
    }
}
