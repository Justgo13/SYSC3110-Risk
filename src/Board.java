import java.util.*;
import java.io.*;

public class Board {
    private ArrayList<Player> players;
    private HashMap<String, Country> countries;
    private HashMap<String, Continent> continents;
    private static final int COUNTRY_COUNT = 42;
    private ArrayList<String> countryNames;
    private ArrayList<String> continentName;

    public Board() {
        players = new ArrayList<>();
        countries = new HashMap<>();
        continents = new HashMap<>();
        countryNames = new ArrayList<>(Arrays.asList(
                "Alaska", "Alberta", "Central America", "Eastern United States", "Greenland", "Northwest Territory", "Ontario",
                "Quebec", "Western United States", "Argentina", "Brazil", "Peru", "Venezuela", "Great Britain", "Iceland",
                "Northern Europe", "Scandinavia", "Southern Europe", "Ukraine", "Western Europe", "Congo", "East Africa", "Egypt",
                "Madagascar", "North Africa", "South Africa", "Afghanistan", "China", "India", "Irkutsk", "Japan", "Kamchatka",
                "Middle East", "Mongolia", "Siam", "Siberia", "Ural", "Yakutsk", "Eastern Australia", "Indonesia", "New Guinea",
                "Western Australia"
        ));
        continentName = new ArrayList<>(Arrays.asList(
                "North America", "South America", "Europe", "Africa", "Asia"
        ));
    }

    public void addPlayer(Player player) {
        players.add(player);
    }

    public void randomizePlayers () {
        Collections.shuffle(players);
    }

    public void buildMap() {
        for (String countryName : countryNames) {
            countries.put(countryName, new Country(countryName));
        }
        for (String continentName : continentName) {
            continents.put(continentName, new Continent(continentName, 0));
        }
    }

    public void placePlayers(int numPlayers) {
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

    public void setAdjacentCountries(){

        try {
            // buffered reader to read the file
            BufferedReader br = new BufferedReader(new FileReader("adjacentCountries.txt"));

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



    public void attack(Country attackingCountry, Country defendingCountry, int numOfAttacker) {

    }
}
