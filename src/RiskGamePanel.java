import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;

/**
 * A view class that represents the map and text area that a user will see change dynamically
 * over the course of the Risk Game
 * @author Jason, Albrara'a
 */
public class RiskGamePanel extends JPanel {
    private HashMap<String, CountryButton> countryButtons;
    private RiskModel model;
    private HashMap<String, Country> countries;
    private JTextArea textArea;
    private ArrayList<JButton> diceJButtons;

    private ArrayList<Image> diceIcons;

    /**
     * Creates the Risk map with country buttons
     * @author Jason
     * @param model The model containing data about the Risk game
     */
    public RiskGamePanel(RiskModel model) {
        this.model = model;
        this.countries = model.getCountries();

        JPanel countryPanel = new JPanel(new GridBagLayout());

        //setting
        setLayout(new GridBagLayout());
        GridBagConstraints viewConstraints = new GridBagConstraints();
        viewConstraints.weighty = 1.0;
        viewConstraints.weightx = 1.0;
        viewConstraints.anchor = GridBagConstraints.FIRST_LINE_START;
        viewConstraints.fill = GridBagConstraints.BOTH;

        countryButtons = new HashMap<>();
        setLayout(new GridBagLayout());
        GridBagConstraints mapConstraints = new GridBagConstraints();
        mapConstraints.weighty = 1.0;
        mapConstraints.weightx = 1.0;
        mapConstraints.anchor = GridBagConstraints.FIRST_LINE_START;

        /*
        NORTH AMERICA
         */

        // Alaska
        mapConstraints.fill = GridBagConstraints.BOTH;
        mapConstraints.insets = new Insets(0,50,0,0);
        mapConstraints.gridx = 0;
        mapConstraints.gridy = 0;
        CountryButton alaska = new CountryButton(countries.get("Alaska"));
        countryButtons.put(alaska.getName(), alaska);
        alaska.setEnabled(false);
        countryPanel.add(alaska, mapConstraints);


        // Northwest Canada
        mapConstraints.insets = new Insets(0,0,0,0);
        mapConstraints.gridx = 1;
        mapConstraints.gridy = 0;
        mapConstraints.gridwidth = 2;
        CountryButton nwCanada = new CountryButton(countries.get("Northwest Territory"));
        countryButtons.put(nwCanada.getName(), nwCanada);
        nwCanada.setEnabled(false);
        countryPanel.add(nwCanada, mapConstraints);

        // Alberta
        mapConstraints.gridx = 1;
        mapConstraints.gridy = 1;
        mapConstraints.gridwidth = 1;
        CountryButton alberta = new CountryButton(countries.get("Alberta"));
        countryButtons.put(alberta.getName(), alberta);
        alberta.setEnabled(false);
        countryPanel.add(alberta, mapConstraints);

        // Ontario
        mapConstraints.gridx = 2;
        mapConstraints.gridy = 1;
        CountryButton ontario = new CountryButton(countries.get("Ontario"));
        countryButtons.put(ontario.getName(), ontario);
        ontario.setEnabled(false);
        countryPanel.add(ontario, mapConstraints);

        // Quebec
        mapConstraints.insets = new Insets(0,0,0,50);
        mapConstraints.gridx = 3;
        mapConstraints.gridy = 1;
        CountryButton quebec = new CountryButton(countries.get("Quebec"));
        countryButtons.put(quebec.getName(), quebec);
        quebec.setEnabled(false);
        countryPanel.add(quebec, mapConstraints);

        // West US
        mapConstraints.insets = new Insets(0,0,0,0);
        mapConstraints.gridx = 1;
        mapConstraints.gridy = 2;
        mapConstraints.gridheight = 2;
        CountryButton wUS = new CountryButton(countries.get("Western United States"));
        countryButtons.put(wUS.getName(), wUS);
        wUS.setEnabled(false);
        countryPanel.add(wUS, mapConstraints);

        // East US
        mapConstraints.gridx = 2;
        mapConstraints.gridy = 2;
        CountryButton eUS = new CountryButton(countries.get("Eastern United States"));
        countryButtons.put(eUS.getName(), eUS);
        eUS.setEnabled(false);
        countryPanel.add(eUS, mapConstraints);

        // Central America
        mapConstraints.gridx = 1;
        mapConstraints.gridy = 4;
        mapConstraints.gridheight = 1;
        CountryButton cAm = new CountryButton(countries.get("Central America"));
        countryButtons.put(cAm.getName(), cAm);
        cAm.setEnabled(false);
        countryPanel.add(cAm, mapConstraints);

        // Greenland
        mapConstraints.insets = new Insets(0,0,0,50);
        mapConstraints.gridheight = 2;
        mapConstraints.gridx = 4;
        mapConstraints.gridy = 0;
        CountryButton greenland = new CountryButton(countries.get("Greenland"));
        countryButtons.put(greenland.getName(), greenland);
        greenland.setEnabled(false);
        countryPanel.add(greenland, mapConstraints);


        /*
        SOUTH AMERICA
         */

        // Venezuela
        mapConstraints.insets = new Insets(0,0,0,0);
        mapConstraints.gridheight = 1;
        mapConstraints.gridx = 2;
        mapConstraints.gridy = 5;
        CountryButton venezuela = new CountryButton(countries.get("Venezuela"));
        countryButtons.put(venezuela.getName(), venezuela);
        venezuela.setEnabled(false);
        countryPanel.add(venezuela, mapConstraints);

        // Peru
        mapConstraints.gridx = 2;
        mapConstraints.gridy = 6;
        CountryButton peru = new CountryButton(countries.get("Peru"));
        countryButtons.put(peru.getName(), peru);
        peru.setEnabled(false);
        countryPanel.add(peru, mapConstraints);

        // Argentina
        mapConstraints.gridheight = 2;
        mapConstraints.gridx = 2;
        mapConstraints.gridy = 7;
        CountryButton argentina = new CountryButton(countries.get("Argentina"));
        countryButtons.put(argentina.getName(), argentina);
        argentina.setEnabled(false);
        countryPanel.add(argentina, mapConstraints);

        // Brazil
        mapConstraints.gridx = 3;
        mapConstraints.gridy = 5;
        CountryButton brazil = new CountryButton(countries.get("Brazil"));
        countryButtons.put(brazil.getName(), brazil);
        brazil.setEnabled(false);
        countryPanel.add(brazil, mapConstraints);

        /*
        EUROPE
         */

        // Iceland
        mapConstraints.gridheight = 1;
        mapConstraints.gridwidth = 1;
        mapConstraints.gridx = 5;
        mapConstraints.gridy = 0;
        CountryButton iceland = new CountryButton(countries.get("Iceland"));
        countryButtons.put(iceland.getName(), iceland);
        iceland.setEnabled(false);
        countryPanel.add(iceland, mapConstraints);

        // Great Britain
        mapConstraints.insets = new Insets(0,50,50,50);
        mapConstraints.gridx = 6;
        mapConstraints.gridy = 1;
        CountryButton gBrit = new CountryButton(countries.get("Great Britain"));
        countryButtons.put(gBrit.getName(), gBrit);
        gBrit.setEnabled(false);
        countryPanel.add(gBrit, mapConstraints);

        // Western Europe
        mapConstraints.insets = new Insets(0,0,50,0);
        mapConstraints.gridx = 6;
        mapConstraints.gridy = 2;
        CountryButton wEur = new CountryButton(countries.get("Western Europe"));
        countryButtons.put(wEur.getName(), wEur);
        wEur.setEnabled(false);
        countryPanel.add(wEur, mapConstraints);

        // South Europe
        mapConstraints.gridx = 7;
        mapConstraints.gridy = 2;
        CountryButton sEur = new CountryButton(countries.get("Southern Europe"));
        countryButtons.put(sEur.getName(), sEur);
        sEur.setEnabled(false);
        countryPanel.add(sEur, mapConstraints);

        // North Europe
        mapConstraints.insets = new Insets(0,0,0,0);
        mapConstraints.gridx = 7;
        mapConstraints.gridy = 1;
        CountryButton nEur = new CountryButton(countries.get("Northern Europe"));
        countryButtons.put(nEur.getName(), nEur);
        nEur.setEnabled(false);
        countryPanel.add(nEur, mapConstraints);

        // Scandinavia
        mapConstraints.gridx = 7;
        mapConstraints.gridy = 0;
        CountryButton scandinavia = new CountryButton(countries.get("Scandinavia"));
        countryButtons.put(scandinavia.getName(), scandinavia);
        scandinavia.setEnabled(false);
        countryPanel.add(scandinavia, mapConstraints);

        // Ukraine
        mapConstraints.gridwidth = 2;
        mapConstraints.gridheight = 3;
        mapConstraints.gridx = 8;
        mapConstraints.gridy = 0;
        CountryButton ukraine = new CountryButton(countries.get("Ukraine"));
        countryButtons.put(ukraine.getName(), ukraine);
        ukraine.setEnabled(false);
        countryPanel.add(ukraine, mapConstraints);

        /*
        ASIA
         */

        // Middle East
        mapConstraints.insets = new Insets(0,25,0,0);
        mapConstraints.gridwidth = 2;
        mapConstraints.gridheight = 3;
        mapConstraints.gridx = 8;
        mapConstraints.gridy = 3;
        CountryButton middleEast = new CountryButton(countries.get("Middle East"));
        countryButtons.put(middleEast.getName(), middleEast);
        middleEast.setEnabled(false);
        countryPanel.add(middleEast, mapConstraints);
        mapConstraints.insets = new Insets(0,0,0,0);

        // Ural
        mapConstraints.gridwidth = 1;
        mapConstraints.gridheight = 2;
        mapConstraints.gridx = 10;
        mapConstraints.gridy = 0;
        CountryButton ural = new CountryButton(countries.get("Ural"));
        countryButtons.put(ural.getName(), ural);
        ural.setEnabled(false);
        countryPanel.add(ural, mapConstraints);

        // Afghanistan
        mapConstraints.gridheight = 1;
        mapConstraints.gridx = 10;
        mapConstraints.gridy = 2;
        CountryButton afghan = new CountryButton(countries.get("Afghanistan"));
        countryButtons.put(afghan.getName(), afghan);
        afghan.setEnabled(false);
        countryPanel.add(afghan, mapConstraints);

        // India
        mapConstraints.gridheight = 2;
        mapConstraints.gridwidth = 1;
        mapConstraints.gridx = 10;
        mapConstraints.gridy = 3;
        CountryButton india = new CountryButton(countries.get("India"));
        countryButtons.put(india.getName(), india);
        india.setEnabled(false);
        countryPanel.add(india, mapConstraints);

        // Siberia
        mapConstraints.gridwidth = 1;
        mapConstraints.gridheight = 2;
        mapConstraints.gridx = 11;
        mapConstraints.gridy = 0;
        CountryButton siberia = new CountryButton(countries.get("Siberia"));
        countryButtons.put(siberia.getName(), siberia);
        siberia.setEnabled(false);
        countryPanel.add(siberia, mapConstraints);

        // China
        mapConstraints.gridheight = 2;
        mapConstraints.gridwidth = 2;
        mapConstraints.gridx = 11;
        mapConstraints.gridy = 3;
        CountryButton china = new CountryButton(countries.get("China"));
        countryButtons.put(china.getName(), china);
        china.setEnabled(false);
        countryPanel.add(china, mapConstraints);

        // Yakutsk
        mapConstraints.gridwidth = 1;
        mapConstraints.gridheight = 1;
        mapConstraints.gridx = 12;
        mapConstraints.gridy = 0;
        CountryButton yakutsk = new CountryButton(countries.get("Yakutsk"));
        countryButtons.put(yakutsk.getName(), yakutsk);
        yakutsk.setEnabled(false);
        countryPanel.add(yakutsk, mapConstraints);

        // Irkutsk
        mapConstraints.gridx = 12;
        mapConstraints.gridy = 1;
        CountryButton irkutsk = new CountryButton(countries.get("Irkutsk"));
        countryButtons.put(irkutsk.getName(), irkutsk);
        irkutsk.setEnabled(false);
        countryPanel.add(irkutsk, mapConstraints);

        // Mongolia
        mapConstraints.gridx = 12;
        mapConstraints.gridy = 2;
        CountryButton mongolia = new CountryButton(countries.get("Mongolia"));
        countryButtons.put(mongolia.getName(), mongolia);
        mongolia.setEnabled(false);
        countryPanel.add(mongolia, mapConstraints);

        // Kamchatka
        mapConstraints.gridheight = 3;
        mapConstraints.gridx = 13;
        mapConstraints.gridy = 0;
        CountryButton kamchatka = new CountryButton(countries.get("Kamchatka"));
        countryButtons.put(kamchatka.getName(), kamchatka);
        kamchatka.setEnabled(false);
        countryPanel.add(kamchatka, mapConstraints);

        // Japan
        mapConstraints.insets = new Insets(0,50,0,0);
        mapConstraints.gridwidth = 1;
        mapConstraints.gridheight = 1;
        mapConstraints.gridx = 14;
        mapConstraints.gridy = 3;
        CountryButton japan = new CountryButton(countries.get("Japan"));
        countryButtons.put(japan.getName(), japan);
        japan.setEnabled(false);
        countryPanel.add(japan, mapConstraints);
        mapConstraints.insets = new Insets(0,0,0,0);

        // Siam
        mapConstraints.gridx = 11;
        mapConstraints.gridy = 5;
        CountryButton siam = new CountryButton(countries.get("Siam"));
        countryButtons.put(siam.getName(), siam);
        siam.setEnabled(false);
        countryPanel.add(siam, mapConstraints);

        /*
        AUSTRALIA
         */

        // Indonesia
        mapConstraints.gridx = 12;
        mapConstraints.gridy = 6;
        CountryButton indonesia = new CountryButton(countries.get("Indonesia"));
        countryButtons.put(indonesia.getName(), indonesia);
        indonesia.setEnabled(false);
        countryPanel.add(indonesia, mapConstraints);

        // New Guinea
        mapConstraints.insets = new Insets(0,50,0,0);
        mapConstraints.gridx = 13;
        mapConstraints.gridy = 6;
        CountryButton newGuinea = new CountryButton(countries.get("New Guinea"));
        countryButtons.put(newGuinea.getName(), newGuinea);
        newGuinea.setEnabled(false);
        countryPanel.add(newGuinea, mapConstraints);
        mapConstraints.insets = new Insets(0,0,0,0);

        // West Australia
        mapConstraints.insets = new Insets(50,0,0,0);
        mapConstraints.gridx = 12;
        mapConstraints.gridy = 7;
        CountryButton wAus = new CountryButton(countries.get("Western Australia"));
        countryButtons.put(wAus.getName(), wAus);
        wAus.setEnabled(false);
        countryPanel.add(wAus, mapConstraints);

        // East Australia
        mapConstraints.insets = new Insets(50,0,0,0);
        mapConstraints.gridx = 13;
        mapConstraints.gridy = 7;
        CountryButton eAus = new CountryButton(countries.get("Eastern Australia"));
        countryButtons.put(eAus.getName(), eAus);
        eAus.setEnabled(false);
        countryPanel.add(eAus, mapConstraints);
        mapConstraints.insets = new Insets(0,0,0,0);

        /*
        AFRICA
         */

        // North Africa
        mapConstraints.gridheight = 2;
        mapConstraints.gridwidth = 2;
        mapConstraints.gridx = 5;
        mapConstraints.gridy = 3;
        CountryButton nAfr = new CountryButton(countries.get("North Africa"));
        countryButtons.put(nAfr.getName(), nAfr);
        nAfr.setEnabled(false);
        countryPanel.add(nAfr, mapConstraints);

        // Congo
        mapConstraints.gridheight = 1;
        mapConstraints.gridwidth = 1;
        mapConstraints.gridx = 6;
        mapConstraints.gridy = 5;
        CountryButton congo = new CountryButton(countries.get("Congo"));
        countryButtons.put(congo.getName(), congo);
        congo.setEnabled(false);
        countryPanel.add(congo, mapConstraints);

        // South Africa
        mapConstraints.gridheight = 2;
        mapConstraints.gridwidth = 2;
        mapConstraints.gridx = 6;
        mapConstraints.gridy = 6;
        CountryButton sAfr = new CountryButton(countries.get("South Africa"));
        countryButtons.put(sAfr.getName(), sAfr);
        sAfr.setEnabled(false);
        countryPanel.add(sAfr, mapConstraints);

        // Egypt
        mapConstraints.gridheight = 1;
        mapConstraints.gridwidth = 1;
        mapConstraints.gridx = 7;
        mapConstraints.gridy = 3;
        CountryButton egypt = new CountryButton(countries.get("Egypt"));
        countryButtons.put(egypt.getName(), egypt);
        egypt.setEnabled(false);
        countryPanel.add(egypt, mapConstraints);

        // East Africa
        mapConstraints.gridheight = 2;
        mapConstraints.gridwidth = 1;
        mapConstraints.gridx = 7;
        mapConstraints.gridy = 4;
        CountryButton eAfr = new CountryButton(countries.get("East Africa"));
        countryButtons.put(eAfr.getName(), eAfr);
        eAfr.setEnabled(false);
        countryPanel.add(eAfr, mapConstraints);

        // Madagascar
        mapConstraints.insets = new Insets(0,50,0,0);
        mapConstraints.gridheight = 1;
        mapConstraints.gridwidth = 1;
        mapConstraints.gridx = 8;
        mapConstraints.gridy = 7;
        CountryButton madagascar = new CountryButton(countries.get("Madagascar"));
        countryButtons.put(madagascar.getName(), madagascar);
        madagascar.setEnabled(false);
        countryPanel.add(madagascar, mapConstraints);
        mapConstraints.insets = new Insets(0,0,0,0);


        viewConstraints.gridx = 0;
        viewConstraints.gridy = 0;
        viewConstraints.gridheight=2;
        add(countryPanel, viewConstraints);

        viewConstraints.gridx = 1;
        viewConstraints.gridy = 0;
        viewConstraints.ipadx = 120;
        viewConstraints.gridheight=1;

        // create a text area for printing game events to a console and added a scrollPane in case of long turns
        textArea = new JTextArea();
        JScrollPane scroll = new JScrollPane(textArea);
        scroll.setPreferredSize(textArea.getPreferredSize());
        textArea.append("It is Player "+ model.getBoard().getPlayers().get(model.getTurnIndex()).getId() +"'s turn\n");
        textArea.setEditable(false);
        add(scroll, viewConstraints);


        // Setting Up Dice Rolls Display

        diceJButtons = new ArrayList<JButton>();

        viewConstraints.gridx = 1;
        viewConstraints.gridy = 1;
        JPanel dicePanel = new JPanel();
        //dicePanel.setComponentOrientation(ComponentOrientation.LEFT_TO_RIGHT);
        add(dicePanel,viewConstraints);


        JButton attackDice1 = new JButton();
        attackDice1.setActionCommand("attack1");
        JButton attackDice2 = new JButton();
        attackDice1.setActionCommand("attack2");
        JButton attackDice3 = new JButton();
        attackDice1.setActionCommand("attack3");
        JButton defendDice1 = new JButton();
        attackDice1.setActionCommand("defend1");
        JButton defendDice2 = new JButton();
        attackDice1.setActionCommand("defend2");

        diceJButtons.add(attackDice1);
        diceJButtons.add(attackDice2);
        diceJButtons.add(attackDice3);
        diceJButtons.add(defendDice1);
        diceJButtons.add(defendDice2);



        dicePanel.setLayout(new GridLayout(4,2));
        dicePanel.add(new JLabel("Attacking Dice"));
        dicePanel.add(new JLabel("Defending Dice"));


        dicePanel.add(attackDice1);
        dicePanel.add(defendDice1);
        dicePanel.add(attackDice2);
        dicePanel.add(defendDice2);
        dicePanel.add(attackDice3);


        // Setting up Dice Icons
        diceIcons = new ArrayList<Image>();

        try{
            for (int i = 1; i< 7;i++) {
                InputStream inputStream = this.getClass().getResourceAsStream(i+ ".png");
                diceIcons.add(ImageIO.read(inputStream));

            }

        }catch(Exception e){
            System.out.println(e);
        }


    }

    public HashMap<String, CountryButton> getCountryButtons(){
        return countryButtons;
    }

    /**
     * Enables the attacking players CountryButton so that they may choose a country to attack from
     * @author Jason
     */
    public void showAttackingCountries() {
        // get all the Country Buttons that the current player owns
        ArrayList<CountryButton> countryButtons = convertCountryToCountryButtons(model.getAttackingPlayer().getCountriesOwned());

        for (CountryButton cb : countryButtons){
            if (cb.getCountry().getPossibleAttacks().size() != 0 && cb.getCountry().getArmySize() >= 2) {
                cb.setEnabled(true);
            }
        }
    }

    /**
     * Highlights all the countries the attacking player can attack from by creating a border around it
     * @author Jason
     * @param e An ActionEvent corresponding to the event created when a CountryButton was clicked
     * @return The number of troops that the player chose to use in attacking the country
     */
    public int showDefendingCountries(ActionEvent e) {
        CountryButton attackingCountry = (CountryButton) e.getSource();

        // converts all countries that the attacking country can attack into their respective country button instance in the view and stores in a list
        ArrayList<CountryButton> defendingCountries = convertCountryToCountryButtons(attackingCountry.getCountry().getAdjacentCountries());
        defendingCountries.forEach(countryButton -> countryButton.setEnabled(true)); // enable all possible countries to attack
        defendingCountries.forEach(countryButton -> countryButton.setBorder(BorderFactory.createLineBorder(Color.yellow, 3)));
        // disables all attacking countries from being pressed
        ArrayList<CountryButton> countryButtons = convertCountryToCountryButtons(model.getAttackingPlayer().getCountriesOwned());
        countryButtons.forEach(countryButton -> countryButton.setEnabled(false));
        countryButtons.forEach(countryButton -> countryButton.setBorder(null));

        // asks for number of troops to attack with
        return Integer.parseInt(getAttackingTroopCount(attackingCountry)); // number of troops to attack with
    }

    /**
     * Handles the behaviour behind executing an attack by calling on the respective method in the model.
     * This method also deals with setting up a continuous attack phase in case the user would like to attack again.
     * @author Jason
     * @param attackingCountry The country the user is attacking from
     */
    public void performAttack(Country attackingCountry) {
        // disable all defending countries of the attacking country
        ArrayList<CountryButton> countryButtons = convertCountryToCountryButtons(attackingCountry.getAdjacentCountries());
        countryButtons.forEach(countryButton -> countryButton.setEnabled(false));
        countryButtons.forEach(countryButton -> countryButton.setBorder(null));

        showAttackingCountries(); // re-enable the user to choose countries for continuous attacking
    }

    /**
     * Takes in countries and returns a list of their corresponding country button instances from RiskView
     * @author Harjap
     * @param countries A list of all countries the player can use to attack
     * @return A list of all the CountryButton instances
     */
    public ArrayList<CountryButton> convertCountryToCountryButtons(ArrayList<Country> countries){
        // get hashmap from view that holds relationship between country name and country Button
        HashMap<String, CountryButton> countryButtonHashMap = this.getCountryButtons();

        // create empty arraylist for return
        ArrayList<CountryButton> countryButtons = new ArrayList<CountryButton>();

        for (Country c: countries){
            countryButtons.add(countryButtonHashMap.get(c.getName()));
        }

        return countryButtons;
    }

    /**
     * Creates a dialog box with a dropdown list asking the user for the number of troops they want to attack with
     * @author Jason
     * @param attackingCountry The country the player is attacking from
     * @return The number of troops the player chose to attack with
     */
    public String getAttackingTroopCount(CountryButton attackingCountry) {
        String[] options = {"OK"};
        int troopCount = attackingCountry.getTroopCount()-1;
        Integer[] troopList = buildTroopDropdownList(troopCount);
        JPanel panel = new JPanel();
        JLabel label = new JLabel("Select the number of troops to attack with: ");
        JComboBox comboBox = new JComboBox(troopList);
        comboBox.setSelectedIndex(0);
        panel.add(label);
        panel.add(comboBox);
        int selectionObject = JOptionPane.showOptionDialog(this, panel, "Choose Troops", JOptionPane.NO_OPTION, JOptionPane.QUESTION_MESSAGE, null, options, options[0]);
        String result;
        while (selectionObject != 0) {
            selectionObject = JOptionPane.showOptionDialog(this, panel, "Choose Troops", JOptionPane.NO_OPTION, JOptionPane.QUESTION_MESSAGE, null, options, options[0]);
        }
        result = comboBox.getSelectedItem().toString();
        return result;
    }

    /**
     * Create a list of Integers with the different amount of troops the player can use to attack with
     * which ranges from 1..n-1
     * @author Jason
     * @param troopCount The number of troops available to attack with
     * @return A Integer list containing the different options of troops the player can attack with
     */
    private Integer[] buildTroopDropdownList(int troopCount) {
        Integer[] troopList = new Integer[troopCount];
        // iterates one less to build a list from 1 - troopCount
        for (int i = 0; i < troopList.length; i++) {
            troopList[i] = troopCount;
            troopCount--;
        }
        return troopList;
    }

    /**
     * Handles an AttackEvent by telling the respective countries to update their visuals which include
     * color and button text
     * @author Albara'a
     * @param ae The AttackEvent which contains information about a completed attack
     */
    public void handleAttackEvent(AttackEvent ae){
        Country attackingCountry = ae.getAttackingCountry();
        Country defendingCountry = ae.getDefendingCountry();


        for (JButton j : countryButtons.values()){
            if (j.getName().equals(attackingCountry.getName())){
                countryButtons.get(attackingCountry.getName()).update();
            }
            else if(j.getName().equals(defendingCountry.getName())){
                countryButtons.get(defendingCountry.getName()).update();
            }
        }


    }

    /**
     * Handles a BattleResultEvent by printing the information to the text area console
     * @author Albara'a
     * @param bre The BattleResultEvent containing information about the battle results
     */
    public void handleResultEvent(BattleResultEvent bre) {
        textArea.append("Here is the results of the battle: \n" +
                "Your country troops remaining: " + bre.getAttackingArmySize() +"\n"+
                "Defending country troops remaining: " + bre.getDefendingArmySize() +"\n\n");
    }

    /**
     * Handles an EndTurnEvent by printing the information to the text area console
     * @author Albara'a
     * @param playerId The next player's ID
     */
    public void handleEndTurn(int playerId){
        // disable all attacking countries of current player
        ArrayList<CountryButton> countryButtons = convertCountryToCountryButtons(model.getAttackingPlayer().getCountriesOwned());
        countryButtons.forEach(countryButton -> countryButton.setEnabled(false));
        textArea.setText("");
        textArea.append("It is Player "+playerId+"'s turn\n");
        //also make sure to display whos turn it is
    }

    /**
     * Handles a DiceRollEvent by printing the attacker and defender max rolls to the text area console
     * @author Albara'a
     *
     * @param de
     */
    public void handleDiceRolls(DiceEvent de) {
        ArrayList<Integer> attackingDice = de.getAttackerDice();
        ArrayList<Integer> defendingDice = de.getDefenderDice();

        Collections.sort(attackingDice, Collections.reverseOrder());
        Collections.sort(defendingDice, Collections.reverseOrder());

        // Clear Dice JButtons before getting new Icons
        for (JButton jb: diceJButtons){
            jb.setVisible(false);
        }

        for (int i=0; i < attackingDice.size(); i++){
            int diceNum = attackingDice.get(i);
            setDiceIcon(diceJButtons.get(i), diceNum);
            diceJButtons.get(i).setVisible(true);
        }

        for (int i=0; i < defendingDice.size(); i++){
            int diceNum = defendingDice.get(i);
            setDiceIcon(diceJButtons.get(i+3), diceNum);
            diceJButtons.get(i+3).setVisible(true);
        }

        String attackNums = "";
        for(int i: attackingDice){
            attackNums += " " + i;
        }

        String defendNums = "";
        for(int i: defendingDice){
            defendNums += " " + i;
        }



        textArea.append("Attacker rolled: " + attackNums+"\n"+
                "Defender rolled: " + defendNums+"\n");


    }

    /**
     * Handles a CountryLostEvent by printing whether or not the player conquered the defending
     * country or not.
     * @author Albara'a
     * @param cle The CountryLostEvent that contains information about a country being taken
     */
    public void handleDefendingCountryLost(CountryLostEvent cle) {
        if(cle.getDefendingCountry().getArmySize() == 0){
            textArea.append("Player " + cle.getAttackingPlayerIndex() + ", you have taken " + cle.getDefendingCountry().getName()
                    + " from Player " + cle.getDefendingCountry().getPlayer().getId()+"\n");
        }else{
            textArea.append("You have failed to conquer "+ cle.getDefendingCountry().getName()+"\n");
        }
    }

    /**
     * Handles the game over event and creates a OptionPane for the player to see who won
     * @param playerID ID of the player who won
     */
    public void handleGameOver(int playerID) {
        JOptionPane.showMessageDialog(this, "Congratulations Player " + playerID + " you won Risk!");
    }

    /**
     * Sets the dice icon corresponding to the attacker and defending dice rolls
     * @author Harjap
     * @param diceJButton The JButton the icon will be placed on
     * @param diceNum The dice value
     */
    public void setDiceIcon(JButton diceJButton, int diceNum){
        ImageIcon icon = new ImageIcon(diceIcons.get(diceNum-1));

        diceJButton.setIcon(icon);

    }

}