import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.util.*;

import static org.junit.Assert.*;

public class RiskModelTest {
    RiskModel rm;

    @Before
    public void setUp() throws Exception {
        rm = new RiskModel(true);
    }

    @After
    public void tearDown() throws Exception {
        rm = null;
    }

    @Test
    public void testDefenderLostCountry(){
        rm.playGame();
        Map<String,Country> testCountries = new HashMap<>();

        //Creating our own country objects so we can specify values
        Country Canada = new  Country("canada");
        testCountries.put("Canada",Canada);
        Country Usa = new  Country("Usa");
        testCountries.put("Usa",Usa);
        Country India = new  Country("India");
        testCountries.put("India",India);

        Canada.addAdjacentCountry(India);
        Canada.addAdjacentCountry(Usa);
        India.addAdjacentCountry(Canada);
        India.addAdjacentCountry(Usa);
        Usa.addAdjacentCountry(India);
        Usa.addAdjacentCountry(Canada);

        Canada.setArmySize(100);
        India.setArmySize(1);
        Usa.setArmySize(1);

        //managing countries owned by the players
        Player player1 = rm.getBoard().getPlayers().get(0);
        Player player2 = rm.getBoard().getPlayers().get(1);

        Canada.setPlayer(player1);
        India.setPlayer(player2);
        Usa.setPlayer(player2);

        //players have an arraylist of countries that they own so we can overwrite the previous values
        player1.setCountriesOwned(new ArrayList<>(Arrays.asList(Canada)));
        player2.setCountriesOwned(new ArrayList<>(Arrays.asList(India,Usa)));

        assertEquals(player1 ,Canada.getPlayer());
        assertEquals(player2 ,India.getPlayer());
        rm.attack(Canada, India,10);
        assertEquals(player1 ,Canada.getPlayer());
        assertEquals(player1 ,India.getPlayer());
    }

    @Test
    public void testPlayerOwnsCountries(){
        rm.playGame();
        Map<String,Country> testCountries = new HashMap<>();

        //Creating our own country objects so we can specify values
        Country Canada = new  Country("canada");
        testCountries.put("Canada",Canada);
        Country Usa = new  Country("Usa");
        testCountries.put("Usa",Usa);
        Country India = new  Country("India");
        testCountries.put("India",India);

        Canada.addAdjacentCountry(India);
        Canada.addAdjacentCountry(Usa);
        India.addAdjacentCountry(Canada);
        India.addAdjacentCountry(Usa);
        Usa.addAdjacentCountry(India);
        Usa.addAdjacentCountry(Canada);

        Canada.setArmySize(100);
        India.setArmySize(1);
        Usa.setArmySize(1);

        //managing countries owned by the players
        Player player1 = rm.getBoard().getPlayers().get(0);
        Player player2 = rm.getBoard().getPlayers().get(1);

        Canada.setPlayer(player1);
        India.setPlayer(player2);
        Usa.setPlayer(player1);

        assertEquals(player1, Usa.getPlayer());
        assertEquals(player1, Canada.getPlayer());
        assertEquals(player2, India.getPlayer());
    }

    @Test
    public void testCountryHasRightPlayer(){
        rm.playGame();
        Map<String,Country> testCountries = new HashMap<>();

        //Creating our own country objects so we can specify values
        Country Canada = new  Country("Canada");
        testCountries.put("Canada",Canada);
        Country India = new  Country("India");
        testCountries.put("India",India);

        Canada.addAdjacentCountry(India);
        India.addAdjacentCountry(Canada);

        //managing countries owned by the players
        Player player1 = rm.getBoard().getPlayers().get(0);
        Player player2 = rm.getBoard().getPlayers().get(1);

        player1.setCountriesOwned(new ArrayList<>(Arrays.asList(Canada)));
        player2.setCountriesOwned(new ArrayList<>(Arrays.asList(India)));

        Canada.setPlayer(player1);
        India.setPlayer(player2);

        assertTrue(player1.getCountriesOwned().contains(Canada));
        assertTrue(player2.getCountriesOwned().contains(India));
    }

    @Test
    public void testNumberOfDice(){
        rm.playGame();

        Map<String,Country> testCountries = new HashMap<>();
        Country Canada = new  Country("canada");
        testCountries.put("Canada",Canada);
        Country Usa = new  Country("Usa");
        testCountries.put("Usa",Usa);

        Canada.addAdjacentCountry(Usa);
        Usa.addAdjacentCountry(Canada);

        Canada.setArmySize(100);
        Usa.setArmySize(3);

        //managing countries owned by the players
        Player player1 = rm.getBoard().getPlayers().get(0);
        Player player2 = rm.getBoard().getPlayers().get(1);

        Canada.setPlayer(player1);
        Usa.setPlayer(player2);

        //checking if number of dice decreases when number of troops changes
        assertEquals(3, rm.attackingDiceInitialization(Canada.getArmySize()).size());
        assertEquals(2, rm.defendingDiceInitialization(Usa.getArmySize()).size());
        //changing attackers number of troops
        Canada.setArmySize(2);
        rm.attack(Canada, Usa,10);
        assertEquals(2, rm.attackingDiceInitialization(Canada.getArmySize()).size());
        assertEquals(1, rm.defendingDiceInitialization(Usa.getArmySize()).size());
    }

    @Test
    public void testAdjacentCountries(){
        rm.playGame();
        Map countries =  rm.getBoard().getCountries();
        assertEquals(Arrays.asList(countries.get("Brazil"),countries.get("Peru")),((Country) countries.get("Argentina")).getAdjacentCountries());
        assertEquals(Arrays.asList(countries.get("Kamchatka"),countries.get("Mongolia")) ,((Country) countries.get("Japan")).getAdjacentCountries());
        assertEquals(Arrays.asList(countries.get("China"),countries.get("India"), countries.get("Indonesia")) ,((Country) countries.get("Siam")).getAdjacentCountries());
        assertEquals(Arrays.asList(countries.get("East Africa"),countries.get("South Africa")),((Country) countries.get("Madagascar")).getAdjacentCountries());
        assertEquals(4,((Country) countries.get("Great Britain")).getAdjacentCountries().size());
    }

    @Test
    public void testContinentsCountries(){
        rm.playGame();
        Map continents =  rm.getBoard().getContinents();
        Map countries =  rm.getBoard().getCountries();
        assertEquals(((Continent) continents.get("North America")).getName(), ((Country) countries.get("Alaska")).getContinent());
        assertEquals(((Continent) continents.get("South America")).getName(), ((Country) countries.get("Peru")).getContinent());
        assertEquals(((Continent) continents.get("Australia")).getName(), ((Country) countries.get("Western Australia")).getContinent());
        assertEquals(("Africa"), ((Country) countries.get("South Africa")).getContinent());

        assertEquals(countries.get("Alaska") , ((Continent) continents.get("North America")).getCountry("Alaska"));
        assertEquals(countries.get("Peru") , ((Continent) continents.get("South America")).getCountry("Peru"));
        assertEquals(countries.get("Western Australia") , ((Continent) continents.get("Australia")).getCountry("Western Australia"));
        assertEquals(countries.get("South Africa") , ((Continent) continents.get("Africa")).getCountry("South Africa"));


    }

    @Test
    public void testCountryStrings(){
        rm.playGame();
        Set actual =  rm.getBoard().getCountries().keySet();
        assertTrue(actual.contains("Alaska"));
        assertTrue(actual.contains("Central America"));
        assertTrue(actual.contains("Peru"));
        assertTrue(actual.contains("Yakutsk"));
        assertTrue(actual.contains("Egypt"));
        assertTrue(actual.contains("Western Australia"));
        assertTrue(actual.size() == 42);
    }

    @Test
    public void testGameWinner(){
        rm.playGame();
        Map<String,Country> testCountries = new HashMap<>();

        //Creating our own country objects so we can specify values
        Country Canada = new  Country("canada");
        testCountries.put("Canada",Canada);
        Country Usa = new  Country("Usa");
        testCountries.put("Usa",Usa);
        Country India = new  Country("India");
        testCountries.put("India",India);

        Canada.addAdjacentCountry(India);
        Canada.addAdjacentCountry(Usa);
        India.addAdjacentCountry(Canada);
        India.addAdjacentCountry(Usa);
        Usa.addAdjacentCountry(India);
        Usa.addAdjacentCountry(Canada);

        Canada.setArmySize(100);
        India.setArmySize(1);
        Usa.setArmySize(1);

        //managing countries owned by the players
        Player player1 = rm.getBoard().getPlayers().get(0);
        Player player2 = rm.getBoard().getPlayers().get(1);

        Canada.setPlayer(player1);
        India.setPlayer(player2);
        Usa.setPlayer(player1);

        //players have an arraylist of countries that they own so we can overwrite the previous values
        player1.setCountriesOwned(new ArrayList<>(Arrays.asList(Canada,Usa)));
        player2.setCountriesOwned(new ArrayList<>(Arrays.asList(India)));

        rm.attack(Canada, India,10);
        assertTrue(rm.getGameOver());
    }
    /*
    @Test
    public void getCountries() {
    }

    @Test
    public void attack() {
    }

    @Test
    public void defendingDiceInitialization() {
    }

    @Test
    public void playGame() {
    }

    @Test
    public void addRiskView() {
    }

    @Test
    public void updateAttackView() {
    }

    @Test
    public void updateBattleResults() {
    }

    @Test
    public void updateDiceResult() {
    }

    @Test
    public void updateCountryLost() {
    }

    @Test
    public void endTurnPhase() {
    }

    @Test
    public void getBoard() {
    }

    @Test
    public void getTurnIndex() {
    }

    @Test
    public void incrementTurnIndex() {
    }
     */
}