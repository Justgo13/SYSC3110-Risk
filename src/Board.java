import org.json.simple.JSONArray;
import org.json.simple.JSONObject;

import java.util.*;

public class Board {
    private static final int COUNTRY_COUNT = 42;
    private static final String JSONCOUNTRYKEY = "countryName";
    private static final String JSONCONTINENTKEY = "continentName";
    private static final String JSONADJACENTKEY = "adjacentCountries";
    private int numOfPlayers;
    private ArrayList<Player> players;
    private HashMap<String, Country> countries;
    private HashMap<String, Continent> continents;
    private ArrayList<String> countryNames;
    private ArrayList<String> continentNames;
    private  HashMap<String, ArrayList<String>> continentCountries;
    private  HashMap<String,Integer> continentBonusArmies;

    /**
    * @author Jason, Harjap, Shashaank
    * Initializes data structures such as HashMaps and ArrayLists to store countries, players, and continents
    */
    public Board() {
        numOfPlayers = 0; // will be changed at runtime
        players = new ArrayList<>();
        countries = new HashMap<>();
        continents = new HashMap<>();
        continentCountries = new HashMap<>();
        countryNames = new ArrayList<>();

        continentBonusArmies = new HashMap<>();
        initializeContinentBonusArmies();

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
     * @param players
     * @param countries
     * @param continents
     */
    public void setupBoard(int numPlayers, List<Player> players, JSONArray countries, JSONArray continents){
        setNumOfPlayers(players.size());
        int totalPlayers = players.size();
        for (int i = 0; i < numPlayers; i++) {
            addPlayer(players.get(i)); // creates players for the game
        }
        randomizePlayers(); // randomizes player order
        for (int i = numPlayers; i < totalPlayers; i++) {
            addPlayer(players.get(i));
        }
        buildContinentNames(continents);
        buildCountryNames(countries);
        buildContinent(countries);
        buildMap(); // adds all countries to map
        placePlayers(totalPlayers); // place players randomly on the map
        setAdjacentCountries(countries);
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
        for (String continentName : continentNames) {
            continents.put(continentName, new Continent(continentName, continentBonusArmies.get(continentName))); // creates continents objects

            //gets all continents and populates them with their specific countries
            //also gives every country the continent it belongs in
            for(int j = 0; j<continentCountries.get(continentName).size(); j++){
                Continent continent = continents.get(continentName);
                continent.addCountry((String) continentCountries.get(continentName).get(j),countries.get(continentCountries.get(continentName).get(j)) ); // populates continents with their specific countries
                countries.get(continentCountries.get(continentName).get(j)).setContinent(continent.getName()); // sets the continent name of each country
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

    private void buildContinent(JSONArray countries) {
        Iterator iterator = countries.iterator();
        JSONObject country;
        while (iterator.hasNext()) {
            country = (JSONObject) iterator.next();
            String continentName = (String) country.get(JSONCONTINENTKEY);
            if (!continentCountries.containsKey(continentName)) {
                continentCountries.put(continentName, new ArrayList<>());
            }
            continentCountries.get(continentName).add((String) country.get(JSONCOUNTRYKEY));
        }
    }

    private void buildCountryNames(JSONArray countries) {
        Iterator iterator = countries.iterator();
        JSONObject country;
        while (iterator.hasNext()) {
            country = (JSONObject) iterator.next();
            countryNames.add((String) country.get(JSONCOUNTRYKEY));
        }
    }

    private void buildContinentNames(JSONArray continents) {
        Iterator iterator = continents.iterator();
        JSONObject continent;
        while (iterator.hasNext()) {
            continent = (JSONObject) iterator.next();
            continentNames.add((String) continent.get(JSONCONTINENTKEY));
        }
    }

        /**
     * For every country in the game, read a .txt file to determine all of the adjacent countries to it. Add all
     * adjacent countries to the corresponding country
     *
     * @author Harjap Gill
     */
    private void setAdjacentCountries(JSONArray countries){
        Iterator iterator = countries.iterator();
        JSONObject country;
        JSONArray adjacentCountries;
        while (iterator.hasNext()) {
            country = (JSONObject) iterator.next();
            adjacentCountries = (JSONArray) country.get(JSONADJACENTKEY);
            Iterator adjacentIterator = adjacentCountries.iterator();
            while (adjacentIterator.hasNext()) {
                Country parentCountry = this.countries.get(country.get(JSONCOUNTRYKEY));
                Country adjacentCountry = this.countries.get(adjacentIterator.next());
                parentCountry.addAdjacentCountry(adjacentCountry);
            }
        }
    }

    public void initializeContinentBonusArmies(){
        continentBonusArmies.put("Asia", 7);
        continentBonusArmies.put("Australia", 2);
        continentBonusArmies.put("North America", 5);
        continentBonusArmies.put("Africa", 3);
        continentBonusArmies.put("South America", 2);
        continentBonusArmies.put("Europe", 5);
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
