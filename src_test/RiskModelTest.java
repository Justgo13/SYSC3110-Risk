import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.util.*;

import static org.junit.Assert.*;

public class RiskModelTest {
    RiskMockModel rm;

    @Before
    public void setUp() {
        rm = new RiskMockModel();
        rm.playGame(2,0);
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
        HumanPlayer P1 = new HumanPlayer( 20, 1);
        HumanPlayer P2 = new HumanPlayer(20, 2);
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
        HumanPlayer P1 = new HumanPlayer( 20, 1);
        HumanPlayer P2 = new HumanPlayer( 20, 2);
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
        Player humanPlayer1 = rm.getBoard().getPlayers().get(0);
        Player humanPlayer2 = rm.getBoard().getPlayers().get(1);

        Canada.setPlayer(humanPlayer1);
        India.setPlayer(humanPlayer2);
        Usa.setPlayer(humanPlayer2);

        //players have an arraylist of countries that they own so we can overwrite the previous values
        humanPlayer1.setCountriesOwned(new ArrayList<>(Collections.singletonList(Canada)));
        humanPlayer2.setCountriesOwned(new ArrayList<>(Arrays.asList(India,Usa)));

        assertEquals(humanPlayer1,Canada.getPlayer());
        assertEquals(humanPlayer2,India.getPlayer());
        rm.attack(Canada, India,10);
        assertEquals(humanPlayer1,Canada.getPlayer());
        assertEquals(humanPlayer1,India.getPlayer());
    }

    @Test
    public void testConnectedCountriesForDuplicates(){
        ArrayList<Country> countries = rm.getConnectedCountries(rm.getCountries().get("China"));
        HashSet sets = new HashSet(countries);
        assertEquals(countries.size(),sets.size());
    }

    @Test
    public void testConnectedCountries(){
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
        Player humanPlayer1 = rm.getBoard().getPlayers().get(0);
        Player humanPlayer2 = rm.getBoard().getPlayers().get(1);

        Canada.setPlayer(humanPlayer1);
        India.setPlayer(humanPlayer2);
        Usa.setPlayer(humanPlayer1);

        rm.reinforce(Canada, India, 30);
        rm.reinforce(Canada, Usa, 50);
        assertEquals(20, Canada.getArmySize());
        assertEquals(51, Usa.getArmySize());
        assertEquals(31, India.getArmySize());
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
        Player humanPlayer1 = rm.getBoard().getPlayers().get(0);
        Player humanPlayer2 = rm.getBoard().getPlayers().get(1);

        Canada.setPlayer(humanPlayer1);
        India.setPlayer(humanPlayer2);
        Usa.setPlayer(humanPlayer1);

        assertEquals(humanPlayer1, Usa.getPlayer());
        assertEquals(humanPlayer1, Canada.getPlayer());
        assertEquals(humanPlayer2, India.getPlayer());
    }

    @Test
    public void testCountryHasRightPlayer(){

        //Creating our own country objects so we can specify values
        Country Canada = new  Country("Canada");
        Country India = new  Country("India");

        Canada.addAdjacentCountry(India);
        India.addAdjacentCountry(Canada);

        //managing countries owned by the players
        Player humanPlayer1 = rm.getBoard().getPlayers().get(0);
        Player humanPlayer2 = rm.getBoard().getPlayers().get(1);

        humanPlayer1.setCountriesOwned(new ArrayList<>(Collections.singletonList(Canada)));
        humanPlayer2.setCountriesOwned(new ArrayList<>(Collections.singletonList(India)));

        Canada.setPlayer(humanPlayer1);
        India.setPlayer(humanPlayer2);

        assertTrue(humanPlayer1.getCountriesOwned().contains(Canada));
        assertTrue(humanPlayer2.getCountriesOwned().contains(India));
    }

    @Test
    public void testNumberOfDice(){

        Country Canada = new  Country("canada");
        Country Usa = new  Country("Usa");

        Canada.addAdjacentCountry(Usa);
        Usa.addAdjacentCountry(Canada);



        //managing countries owned by the players
        Player humanPlayer1 = rm.getBoard().getPlayers().get(0);
        Player humanPlayer2 = rm.getBoard().getPlayers().get(1);

        Canada.setPlayer(humanPlayer1);
        Usa.setPlayer(humanPlayer2);

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
    public void testBonusTroopCalculationNoCountries() {
        Player p1 = new HumanPlayer(0,1);
        assertEquals(3,rm.bonusTroopCalculation(p1));
    }

    @Test
    public void testBonusTroopCalculationCountriesNoCotinent() {
        Player p1 = new HumanPlayer(0,1);
        Country bonusCountry1 = new Country("");
        Country bonusCountry2 = new Country("");
        Country bonusCountry3 = new Country("");
        p1.addCountry(bonusCountry1);
        p1.addCountry(bonusCountry2);
        p1.addCountry(bonusCountry3);
        assertEquals(4,rm.bonusTroopCalculation(p1));
    }

    @Test
    public void testBonusTroopCalculationOneContinent() {
        Collection<Continent> continents = rm.getBoard().getContinents().values();
        Continent bonusContinent = (Continent) continents.toArray()[0];
        bonusContinent.getCountries().clear();

        Player p1 = new HumanPlayer(0,1);
        Country bonusCountry1 = new Country("");
        Country bonusCountry2 = new Country("");
        Country bonusCountry3 = new Country("");
        bonusCountry1.setPlayer(p1);
        bonusCountry2.setPlayer(p1);
        bonusCountry3.setPlayer(p1);
        bonusCountry1.setContinent(bonusContinent.getName());
        bonusCountry2.setContinent(bonusContinent.getName());
        bonusCountry3.setContinent(bonusContinent.getName());

        bonusContinent.addCountry("country1",bonusCountry1);
        bonusContinent.addCountry("country2",bonusCountry2);
        bonusContinent.addCountry("country3",bonusCountry3);
        p1.addCountry(bonusCountry1);
        p1.addCountry(bonusCountry2);
        p1.addCountry(bonusCountry3);
        assertEquals(6,rm.bonusTroopCalculation(p1));
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

        Player humanPlayer1 = rm.getBoard().getPlayers().get(0);
        Player humanPlayer2 = rm.getBoard().getPlayers().get(1);

        Canada.setPlayer(humanPlayer1);
        India.setPlayer(humanPlayer2);
        Usa.setPlayer(humanPlayer1);

        humanPlayer1.setCountriesOwned(new ArrayList<>(Arrays.asList(Canada,Usa)));
        humanPlayer2.setCountriesOwned(new ArrayList<>(Arrays.asList(India)));

        rm.attack(Canada, India,10);
        assertTrue(rm.getGameOver());
    }

}