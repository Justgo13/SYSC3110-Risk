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
    public void testCountriesAreCorrect(){
        Board board = new Board();
        Collection actual =  board.getCountries().values();
        System.out.println(actual);
    }

    @Test
    public void testGameWinner(){
        rm.playGame();
        Map<String,Country> testCountries = new HashMap<>();

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

        Player player1 = rm.getBoard().getPlayers().get(0);
        Player player2 = rm.getBoard().getPlayers().get(1);

        Canada.setPlayer(player1);
        India.setPlayer(player2);
        Usa.setPlayer(player1);

        player1.setCountriesOwned(new ArrayList<>(Arrays.asList(Canada,Usa)));
        player2.setCountriesOwned(new ArrayList<>(Arrays.asList(India)));

        rm.attack(Canada, India,10);
        System.out.println(rm.getBoard().getPlayers());
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