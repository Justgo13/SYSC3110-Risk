import javax.swing.*;
import java.util.*;

public class RiskModel {
    private Board board;
    private int turnIndex;

    private List<RiskView> views;

    /**
     * Creates an instance of the Risk game
     */
    public RiskModel()
    {
        board = new Board();
        turnIndex = 0;
        views = new ArrayList<>();
    }

    public HashMap<String,Country> getCountries(){
        return board.getCountries();
    }

    /**
     * The attack method simulates the risk battle between one attacking country and one defending country
     * by randomly generating dice values corresponding to how many attacker and defender there are.
     * The board will then be updated by notifying the respective view.
     *
     * @author Jason Gao, Albara'a
     *
     * @param attackingCountry Country that is initializing the battle
     * @param defendingCountry Country that is defending against an attack
     * @param numOfAttackers The number of attackers the attacking country is attacking with
     */

    public void attack(Country attackingCountry, Country defendingCountry, int numOfAttackers) {
        /*
        defending 2+ = 2 die
        defending 1 = 1 die
        attacking n = n die
        highest attacker dice roll competes with highest defender dice
        second highest attacker fights second highest defender
        attacker : 5,5,1 defender : 6,5
           - 6 fight 5 remove attacker
           - 5 fight 5 remove attacker on tie
         */
        ArrayList<Integer> attackerDice = attackingDiceInitialization(numOfAttackers);
        ArrayList<Integer> defenderDice = defendingDiceInitialization(defendingCountry.getArmySize());

        attackPhase(defendingCountry, attackingCountry, attackerDice, defenderDice, numOfAttackers);

        checkDefenderLostCountry(defendingCountry, attackingCountry, numOfAttackers);

        updateBattleResults(defendingCountry.getArmySize(), attackingCountry.getArmySize());

        if(board.checkWinner()){
            System.out.println("Congratulations, "+board.getPlayers().get(0).getName()+". You won!");
            System.exit(0);
        }
        updateAttackView(attackingCountry, defendingCountry);
    }

    /**
     * Initializes the defending country dice roll through random generation
     * @author Albara'a
     * @param defendingArmySize the size of the defending army
     * @return List of the randomly generated dice rolls
     */
    public ArrayList<Integer> defendingDiceInitialization(int defendingArmySize){
        Random random = new Random();
        ArrayList<Integer> defenderDice = new ArrayList<>();
        if (defendingArmySize >= 2) {
            defenderDice.add(random.nextInt(6)+1);
            defenderDice.add(random.nextInt(6)+1);
        } else if (defendingArmySize == 1) {
            defenderDice.add(random.nextInt(6)+1);
        }

        return defenderDice;
    }

    /**
     * Initializes the attacking country dice roll through random generation
     * @author Albara'a
     * @param numOfAttackers the size of the attacking army
     * @return List of the randomly generated dice rolls
     */
    private  ArrayList<Integer> attackingDiceInitialization(int numOfAttackers){
        Random random = new Random();
        ArrayList<Integer> attackerDice = new ArrayList<>();
        // initialize attacker dice values
        for (int i = 0; i < numOfAttackers; i++) {
            attackerDice.add(random.nextInt(6)+1);
        }

        return attackerDice;
    }

    /**
     * Performs the attack between the attacking country and defending country and will remove troops from their
     * respective countries based on the result of the dice rolls.
     * @author Albara'a
     * @param defenderDice the defending country's dice rolls
     * @param attackerDice the attacking country's dice rolls
     * @param defendingCountry the country that is defending
     * @param attackingCountry the country that is attacking
     * @param numOfAttackers the size of the attacking army
     */
    private void attackPhase(Country defendingCountry, Country attackingCountry, ArrayList<Integer> attackerDice, ArrayList<Integer> defenderDice, int numOfAttackers){
        for (int i = defenderDice.size(); i > 0; i--) {
            if (attackerDice.size() == 0) {
                break;
            }
            Integer attackerMax = Collections.max(attackerDice);
            Integer defenderMax = Collections.max(defenderDice);
            updateDiceResult(attackerDice, defenderDice);
            if (attackerMax > defenderMax) { // attackers win
                defendingCountry.removeArmy();
            } else { // defenders win
                attackingCountry.removeArmy();
                numOfAttackers--;
            }

            // remove the dice from the current attacking phase
            attackerDice.remove(attackerMax);
            defenderDice.remove(defenderMax);
        }

    }

    /**
     * Once the defending country is seized by the attacking player, the number of troops remaining in the attack
     * will move into the defending country.
     * @author Albara'a
     * @param defendingCountry the defending country
     * @param attackingCountry the attacking country
     * @param numOfAttackers the size of the attacking army
     */
    private void checkDefenderLostCountry(Country defendingCountry, Country attackingCountry, int numOfAttackers){
        updateCountryLost(defendingCountry, board.getPlayers().get(turnIndex).getId());
        if (defendingCountry.getArmySize() == 0) {
            defendingCountry.getPlayer().removeCountry(defendingCountry); // removes the lost country from the defending player
            defendingCountry.setPlayer(attackingCountry.getPlayer()); // assigns country to the dominating player
            board.getPlayers().get(turnIndex).addCountry(defendingCountry);// add new country to the dominating player
            int attackersStayed = attackingCountry.getArmySize() - numOfAttackers;
            defendingCountry.setArmySize(numOfAttackers); // moves remaining attackers to conquered country
            attackingCountry.setArmySize(attackersStayed); // removes attackers from original country
            board.checkEliminated();
        }

    }

    /**
     * Initial method that is called to start the risk game
     * @author Jason
     */
    public void playGame() {
        String[] options = {"OK"};
        final String[] players = {"2","3","4","5","6"};
        JPanel panel = new JPanel();
        JLabel label = new JLabel("Select the number of players: ");
        JComboBox comboBox = new JComboBox(players);
        comboBox.setSelectedIndex(0);
        panel.add(label);
        panel.add(comboBox);
        int numPlayers = JOptionPane.showOptionDialog(null, panel, "Choose Troops", JOptionPane.NO_OPTION, JOptionPane.QUESTION_MESSAGE, null, options, options[0]);
        String result = "0";
        while (numPlayers != 0) {
            numPlayers = JOptionPane.showOptionDialog(null, panel, "Choose Troops", JOptionPane.NO_OPTION, JOptionPane.QUESTION_MESSAGE, null, options, options[0]);
        }
        if (numPlayers == 0) {
            result = comboBox.getSelectedItem().toString();
        }
        board.setupBoard(Integer.parseInt(result));
    }

    /**
     * Adds a view to listen to changes in the model
     * @author Harjap
     * @param view The view instance containing the map and text area console
     */
    public void addRiskView(RiskView view){
        views.add(view);
    }

    /**
     * Notifies the view that an AttackEvent has occurred
     * @author Albara'a
     * @param attackingCountry The attacking country
     * @param defendingCountry The country being attacked
     */
    public void updateAttackView(Country attackingCountry, Country defendingCountry){
        for (RiskView v : views){
            v.handleAttackEvent(new AttackEvent(this,attackingCountry,defendingCountry));
        }
    }

    /**
     * Notifies the view that a battle result event needs to be printed
     * @author Albara'a
     * @param defendingArmySize The defending country army size
     * @param attackingArmySize The attacking coutnry army size
     */
    public void updateBattleResults(int defendingArmySize, int attackingArmySize){
        for(RiskView v : views){
            v.handleResultEvent(new BattleResultEvent(this,attackingArmySize, defendingArmySize));
        }
    }

    /**
     * Notifying the views to handle a dice roll event
     * @author Albara'a
     *
     * @param attackerDice arraylist of the attackers dice rolls
     * @param defenderDice arraylist of the defenders dice rolls
     */
    public void updateDiceResult(ArrayList<Integer> attackerDice, ArrayList<Integer> defenderDice){
        for(RiskView v : views){
            v.handleDiceRolls(new DiceEvent(this,attackerDice,defenderDice));
        }
    }

    /**
     * Notifies the view to handle a country being taken over event
     * @author Albara'a
     * @param defendingCountry The defending country that was taken
     * @param attackingPlayerIndex The player who took the country
     */
    public void updateCountryLost(Country defendingCountry, int attackingPlayerIndex){
        for(RiskView v: views){
            v.handleDefendingCountryLost(new CountryLostEvent(this, defendingCountry, attackingPlayerIndex));
        }

    }

    /**
     * Notifies the view that a turn has ended
     * @author Albara'a
     * @param playerID The ID of the next player
     */
    public void endTurnPhase(int playerID){
        for(RiskView v: views){
            v.handleEndTurn(playerID);
        }
    }

    public Board getBoard() {
        return board;
    }

    public int getTurnIndex() {
        return turnIndex;
    }

    public void incrementTurnIndex() {
        this.turnIndex = (1+turnIndex) % board.getNumOfPlayers();
    }
}
