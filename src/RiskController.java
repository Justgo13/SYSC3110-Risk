import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.HashMap;

public class RiskController implements ActionListener{
    private RiskGame riskModel;
    private RiskView riskView;
    private Country attackingCountry;
    private Country defendingCountry;
    private JButton attack;
    private int troopCount;
    private enum attackState {
        SHOW_PLAYER_COUNTRIES, SHOW_DEFENDING_COUNTRIES, COMMENCE_ATTACK
    }
    private attackState currentState;

    public RiskController(RiskGame riskModel, RiskView riskView){
        this.riskModel = riskModel;
        this.riskView = riskView;
        this.currentState = null;
    }

    /**
     * Takes in countries and returns a list of their corresponding country button instances from RiskView
     * @param countries A list of all countries the player can use to attack
     * @return A list of all the CountryButton instances
     */
    public ArrayList<CountryButton> convertCountryToCountryButtons(ArrayList<Country> countries){
        // get hashmap from view that holds relationship between country name and country Button
        HashMap<String, CountryButton> countryButtonHashMap = riskView.getCountryButtons();

        // create empty arraylist for return
        ArrayList<CountryButton> countryButtons = new ArrayList<CountryButton>();

        for (Country c: countries){
            countryButtons.add(countryButtonHashMap.get(c.getName()));
        }

        return countryButtons;
    }

    private Player getAttackingPlayer() {
        int turnIndex = riskModel.getTurnIndex();
        ArrayList<Player> players = riskModel.getBoard().getPlayers();
        return players.get(turnIndex);
    }

    private void showAttackingCountries() {
        // get all the Country Buttons that the current player owns
        ArrayList<CountryButton> countryButtons = convertCountryToCountryButtons(getAttackingPlayer().getCountriesOwned());

        for (CountryButton cb : countryButtons){
            if (cb.getCountry().getPossibleAttacks().size() != 0 && cb.getCountry().getArmySize() >= 2) {
                cb.setEnabled(true);
            }
        }
        currentState = attackState.SHOW_DEFENDING_COUNTRIES;
    }

    private int showDefendingCountries(ActionEvent e) {
        CountryButton attackingCountry = (CountryButton) e.getSource();

        // converts all countries that the attacking country can attack into their respective country button instance in the view and stores in a list
        ArrayList<CountryButton> defendingCountries = convertCountryToCountryButtons(attackingCountry.getCountry().getAdjacentCountries());
        defendingCountries.forEach(countryButton -> countryButton.setEnabled(true)); // enable all possible countries to attack

        // disables all attacking countries from being pressed
        ArrayList<CountryButton> countryButtons = convertCountryToCountryButtons(getAttackingPlayer().getCountriesOwned());
        countryButtons.forEach(countryButton -> countryButton.setEnabled(false));

        currentState = attackState.COMMENCE_ATTACK;

        // asks for number of troops to attack with
        return Integer.parseInt(getAttackingTroopCount(attackingCountry)); // number of troops to attack with
    }

    private String getAttackingTroopCount(CountryButton attackingCountry) {
        String[] options = {"OK"};
        int troopCount = attackingCountry.getTroopCount()-1;
        Integer[] troopList = buildTroopDropdownList(troopCount);
        JPanel panel = new JPanel();
        JLabel label = new JLabel("Select the number of troops to attack with: ");
        JComboBox comboBox = new JComboBox(troopList);
        comboBox.setSelectedIndex(0);
        panel.add(label);
        panel.add(comboBox);
        int selectionObject = JOptionPane.showOptionDialog(riskView, panel, "Choose Troops", JOptionPane.NO_OPTION, JOptionPane.QUESTION_MESSAGE, null, options, options[0]);
        String result = "0";
        if (selectionObject == JOptionPane.CLOSED_OPTION) {
        }
        if (selectionObject == 0) {
            result = comboBox.getSelectedItem().toString();
        }
        return result;
    }

    private Integer[] buildTroopDropdownList(int troopCount) {
        Integer[] troopList = new Integer[troopCount];
        // iterates one less to build a list from 1 - troopCount
        for (int i = 0; i < troopList.length; i++) {
            troopList[i] = troopCount;
            troopCount--;
        }
        return troopList;
    }

    private void performAttack(Country attackingCountry, Country defendingCountry, int troopCount) {
        riskModel.attack(attackingCountry, defendingCountry, troopCount);

        // disable all defending countries of the attacking country
        ArrayList<CountryButton> countryButtons = convertCountryToCountryButtons(attackingCountry.getAdjacentCountries());
        countryButtons.forEach(countryButton -> countryButton.setEnabled(false));

        showAttackingCountries(); // re-enable the user to choose countries for continuous attacking
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if(e.getActionCommand().equals("country")) {
            CountryButton countryButton = (CountryButton) e.getSource();
            // Dealing with countries being clicked
            //identify what turn it is
            if (currentState == attackState.SHOW_DEFENDING_COUNTRIES) {
                attackingCountry = countryButton.getCountry();
                troopCount = showDefendingCountries(e);
            } else if (currentState == attackState.COMMENCE_ATTACK) {
                defendingCountry = countryButton.getCountry();
                performAttack(attackingCountry,defendingCountry,troopCount);
            }
        }else if (e.getActionCommand().equals("endturn")){
            if (currentState == attackState.SHOW_DEFENDING_COUNTRIES || currentState == null) {
                // disable all attacking countries of current player
                ArrayList<CountryButton> countryButtons = convertCountryToCountryButtons(getAttackingPlayer().getCountriesOwned());
                countryButtons.forEach(countryButton -> countryButton.setEnabled(false));

                riskModel.incrementTurnIndex(); // moves to next player
                riskModel.endTurnPhase(riskModel.getBoard().getPlayers().get(riskModel.getTurnIndex()).getId());
                attack.setEnabled(true);
            }
        } else if (e.getActionCommand().equals("attack")){
            // show all countries that we can attack with
            // this button should get disabled after until the attacking phase has been completed
            currentState = attackState.SHOW_PLAYER_COUNTRIES;
            attack = (JButton) e.getSource();
            attack.setEnabled(false);
            if (currentState == attackState.SHOW_PLAYER_COUNTRIES)
                showAttackingCountries();
        }

    }

    public void addActionListener(JButton component){
        component.addActionListener(this);

    }

}
