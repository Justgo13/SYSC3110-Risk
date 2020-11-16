import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.util.*;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

public class RiskModelTest {
    RiskMockModel rm;

    @Before
    public void setUp() {
        rm = new RiskMockModel();
        rm.playGame(2);
    }

    @After
    public void tearDown() {
        rm = null;
    }

    @Test
    public void testDefendDiceOneTroop(){
        assertEquals(1,rm.defendingDiceInitialization(1).size());
    }

    @Test
    public void testDefendDiceTwoTroop(){
        assertEquals(2,rm.defendingDiceInitialization(2).size());
        assertEquals(2,rm.defendingDiceInitialization(3).size());
        assertEquals(2,rm.defendingDiceInitialization(7).size());
    }

    @Test
    public void testAttackDiceOneTroop(){
        assertEquals(1,rm.attackingDiceInitialization(1).size());
    }

    @Test
    public void testAttackDiceTwoTroops(){
        assertEquals(2,rm.attackingDiceInitialization(2).size());
    }

    @Test
    public void testAttackDiceThreeTroops(){
        assertEquals(3,rm.attackingDiceInitialization(3).size());
        assertEquals(3,rm.attackingDiceInitialization(5).size());
        assertEquals(3,rm.attackingDiceInitialization(7).size());
    }

    @Test
    public void testAttackerWinPhase(){
        Country A1 = new Country("Attacker");
        Country D1 = new Country("Defender");
        A1.setArmySize(5);
        D1.setArmySize(5);
        rm.attackPhase(D1,A1,new ArrayList<>(Arrays.asList(6,5,4)),new ArrayList<>(Arrays.asList(5,4)),4);
        assertEquals(5, A1.getArmySize());
        assertEquals(3, D1.getArmySize());
    }

    @Test
    public void testAttackerLosePhase(){
        Country A1 = new Country("Attacker");
        Country D1 = new Country("Defender");
        A1.setArmySize(5);
        D1.setArmySize(5);
        rm.attackPhase(D1,A1,new ArrayList<>(Arrays.asList(6,5,4)),new ArrayList<>(Arrays.asList(6,5)),4);
        assertEquals(3, A1.getArmySize());
        assertEquals(5, D1.getArmySize());
    }

    @Test
    public void testAttackerTiePhase(){
        Country A1 = new Country("Attacker");
        Country D1 = new Country("Defender");
        A1.setArmySize(5);
        D1.setArmySize(5);
        rm.attackPhase(D1,A1,new ArrayList<>(Arrays.asList(6,5,4)),new ArrayList<>(Arrays.asList(6,4)),4);
        assertEquals(4, A1.getArmySize());
        assertEquals(4, D1.getArmySize());
    }

    @Test
    public void testAttackerTakesDefenderCountry(){
        Country A1 = new Country("Attacker");
        Country D1 = new Country("Defender");
        A1.setArmySize(5);
        D1.setArmySize(0);
        Player P1 = new Player("P1", 20, 1);
        Player P2 = new Player("P2", 20, 2);
        A1.setPlayer(P1);
        D1.setPlayer(P2);
        rm.checkDefenderLostCountry(D1,A1,4);
        assertEquals(P1, A1.getPlayer());
        assertEquals(P1, D1.getPlayer());
    }

    @Test
    public void testAttackerFailsToTakeDefenderCountry(){
        Country A1 = new Country("Attacker");
        Country D1 = new Country("Defender");
        A1.setArmySize(5);
        D1.setArmySize(1);
        Player P1 = new Player("P1", 20, 1);
        Player P2 = new Player("P2", 20, 2);
        A1.setPlayer(P1);
        D1.setPlayer(P2);
        rm.checkDefenderLostCountry(D1,A1,4);
        assertEquals(P1, A1.getPlayer());
        assertEquals(P2, D1.getPlayer());
    }
    //end of unit style tests and more Functional style testing is below


    @Test
    public void testDefenderLostCountry(){

        //Creating our own country objects so we can specify values
        Country Canada = new  Country("canada");
        Country Usa = new  Country("Usa");
        Country India = new  Country("India");

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
        player1.setCountriesOwned(new ArrayList<>(Collections.singletonList(Canada)));
        player2.setCountriesOwned(new ArrayList<>(Arrays.asList(India,Usa)));

        assertEquals(player1 ,Canada.getPlayer());
        assertEquals(player2 ,India.getPlayer());
        rm.attack(Canada, India,10);
        assertEquals(player1 ,Canada.getPlayer());
        assertEquals(player1 ,India.getPlayer());
    }

    @Test
    public void testGroupOfNeighbouringCountries(){

        rm.getPlayersAdjacentCountries(rm.getCountries().get("China"));
    }

    @Test
    public void testPlayerOwnsCountries(){

        //Creating our own country objects so we can specify values
        Country Canada = new  Country("canada");
        Country Usa = new  Country("Usa");
        Country India = new  Country("India");

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

        //Creating our own country objects so we can specify values
        Country Canada = new  Country("Canada");
        Country India = new  Country("India");

        Canada.addAdjacentCountry(India);
        India.addAdjacentCountry(Canada);

        //managing countries owned by the players
        Player player1 = rm.getBoard().getPlayers().get(0);
        Player player2 = rm.getBoard().getPlayers().get(1);

        player1.setCountriesOwned(new ArrayList<>(Collections.singletonList(Canada)));
        player2.setCountriesOwned(new ArrayList<>(Collections.singletonList(India)));

        Canada.setPlayer(player1);
        India.setPlayer(player2);

        assertTrue(player1.getCountriesOwned().contains(Canada));
        assertTrue(player2.getCountriesOwned().contains(India));
    }

    @Test
    public void testNumberOfDice(){

        Country Canada = new  Country("canada");
        Country Usa = new  Country("Usa");

        Canada.addAdjacentCountry(Usa);
        Usa.addAdjacentCountry(Canada);



        //managing countries owned by the players
        Player player1 = rm.getBoard().getPlayers().get(0);
        Player player2 = rm.getBoard().getPlayers().get(1);

        Canada.setPlayer(player1);
        Usa.setPlayer(player2);

        Canada.setArmySize(100);
        Usa.setArmySize(3);

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
        HashMap countries =  rm.getBoard().getCountries();
        assertEquals(Arrays.asList(countries.get("Brazil"),countries.get("Peru")),((Country) countries.get("Argentina")).getAdjacentCountries());
        assertEquals(Arrays.asList(countries.get("Kamchatka"),countries.get("Mongolia")) ,((Country) countries.get("Japan")).getAdjacentCountries());
        assertEquals(Arrays.asList(countries.get("China"),countries.get("India"), countries.get("Indonesia")) ,((Country) countries.get("Siam")).getAdjacentCountries());
        assertEquals(Arrays.asList(countries.get("East Africa"),countries.get("South Africa")),((Country) countries.get("Madagascar")).getAdjacentCountries());
        assertEquals(4,((Country) countries.get("Great Britain")).getAdjacentCountries().size());
    }

    @Test
    public void testContinentsCountries(){
        HashMap continents =  rm.getBoard().getContinents();
        HashMap countries =  rm.getBoard().getCountries();
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
        Set actual =  rm.getBoard().getCountries().keySet();
        assertTrue(actual.contains("Alaska"));
        assertTrue(actual.contains("Central America"));
        assertTrue(actual.contains("Peru"));
        assertTrue(actual.contains("Yakutsk"));
        assertTrue(actual.contains("Egypt"));
        assertTrue(actual.contains("Western Australia"));
        assertEquals(42,actual.size());
    }

    @Test
    public void testGameWinner(){

        Country Canada = new  Country("canada");
        Country Usa = new  Country("Usa");
        Country India = new  Country("India");

        Canada.addAdjacentCountry(India);
        Canada.addAdjacentCountry(Usa);
        India.addAdjacentCountry(Canada);
        India.addAdjacentCountry(Usa);
        Usa.addAdjacentCountry(India);
        Usa.addAdjacentCountry(Canada);

        Canada.setArmySize(100);
        India.setArmySize(1);
        Usa.setArmySize(1);

        Player player1 = rm.getBoard().getPlayers().get(0);
        Player player2 = rm.getBoard().getPlayers().get(1);

        Canada.setPlayer(player1);
        India.setPlayer(player2);
        Usa.setPlayer(player1);

        player1.setCountriesOwned(new ArrayList<>(Arrays.asList(Canada,Usa)));
        player2.setCountriesOwned(new ArrayList<>(Arrays.asList(India)));

        rm.attack(Canada, India,10);
        assertTrue(rm.getGameOver());
    }

}