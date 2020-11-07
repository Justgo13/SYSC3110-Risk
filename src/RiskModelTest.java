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
    public void testAdjacentCountries(){
        rm.playGame();
        Map countries =  rm.getBoard().getCountries();
        assertEquals(((Country) countries.get("Argentina")).getAdjacentCountries(),Arrays.asList(countries.get("Brazil"),countries.get("Peru")));
        assertEquals(((Country) countries.get("Japan")).getAdjacentCountries(),Arrays.asList(countries.get("Kamchatka"),countries.get("Mongolia")) );
        assertEquals(((Country) countries.get("Siam")).getAdjacentCountries(),Arrays.asList(countries.get("China"),countries.get("India"), countries.get("Indonesia")) );
        assertEquals(((Country) countries.get("Madagascar")).getAdjacentCountries(),Arrays.asList(countries.get("East Africa"),countries.get("South Africa")));
        assertTrue(((Country) countries.get("Great Britain")).getAdjacentCountries().size() == 4);

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