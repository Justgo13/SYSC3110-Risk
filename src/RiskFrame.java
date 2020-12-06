import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;

import javax.imageio.ImageIO;
import javax.swing.*;
import javax.swing.filechooser.FileNameExtensionFilter;
import java.awt.*;
import java.io.*;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;

/**
 * Creates a JFrame for the Risk game containing a button panel and view class
 * @author Jason
 */
public class RiskFrame extends JFrame implements RiskView{
    private static final int MIN_PLAYERS = 1;
    private static final int MAX_PLAYERS = 6;
    private static final int THICKNESS = 3;
    private static final int MIN_ARMY_SIZE = 2;
    private RiskModel model;
    private HashMap<String, CountryButton> countryButtons;
    private HashMap<String, Country> countries;
    private JTextArea textArea;
    private ArrayList<JButton> diceJButtons;
    private transient ArrayList<Image> diceIcons;
    private JButton attack;
    private JButton endPhase;
    private JButton reinforce;
    private JButton placeTroops;
    private GridBagConstraints mapConstraints;
    private JSONObject mapJSON;
    public RiskFrame() {
        super(PlayGame.GAME_TITLE.toString());
        setLayout(new GridBagLayout());

        mapJSON = null;
        boolean loadingChoice = generalGameInitPopup(PlayGame.LOAD_GAME_POPUP.toString());
        if(loadingChoice){
            File file = chooseFile();
            model = loadFromFile(file);
            mapJSON = model.getJsonObject();

            model.removeAllRiskView();
            model.addRiskView(this); // Adds the view to the model
        } else{
            model = new RiskModel();
            boolean customMapChoice = generalGameInitPopup(PlayGame.LOAD_MAP_POPUP.toString());
            if (customMapChoice) { // choose a custom map
                // loops you until a valid map is given
                while (true) {
                    mapJSON = parseFile();
                    if (model.validateJSONMap(mapJSON)) {
                        break;
                    } else {
                        JOptionPane.showMessageDialog(this, PlayGame.INVALID_MAP_MESSAGE.toString());
                    }
                }
                model.setJsonObject(mapJSON);

            }else{ // use the basic world map
                // ask for player number
                int[] getPlayerList = invokePlayerPopup();
                int numPlayer = getPlayerList[0];
                int aiPlayer = getPlayerList[1];
                JSONParser parser = new JSONParser();

                try{
                    InputStream inputStream = this.getClass().getResourceAsStream(JSONConstants.DEFAULT_FILE.toString());
                    Object obj = parser.parse(new InputStreamReader(inputStream, StandardCharsets.UTF_8));
                    JSONObject jsonMap = (JSONObject) obj;
                    model.setJsonObject(jsonMap);
                    mapJSON = jsonMap;
                }catch(Exception e){
                    System.out.println(e);
                }
                model.playGame(numPlayer, aiPlayer);
                model.addRiskView(this); // Adds the view to the model
            }
        }

        // Creates the Controller
        RiskController riskController = new RiskController(model);

        // Creating a constraint for the entire frame
        GridBagConstraints frameConstraint = new GridBagConstraints();
        frameConstraint.weighty = 1.0;
        frameConstraint .weightx = 1.0;
        frameConstraint .anchor = GridBagConstraints.FIRST_LINE_START;
        frameConstraint .fill = GridBagConstraints.BOTH;

        // create map
        this.countries = model.getCountries();
        JPanel gamePanel = new JPanel(new GridBagLayout());
        JPanel countryPanel = new JPanel(new GridBagLayout());

        //setting
        GridBagConstraints gamePanelConstraints = new GridBagConstraints();
        gamePanelConstraints.weighty = 1.0;
        gamePanelConstraints.weightx = 1.0;
        gamePanelConstraints.anchor = GridBagConstraints.FIRST_LINE_START;
        gamePanelConstraints.fill = GridBagConstraints.BOTH;

        countryButtons = new HashMap<>();
        setLayout(new GridBagLayout());
        mapConstraints = new GridBagConstraints();
        mapConstraints.weighty = 1.0;
        mapConstraints.weightx = 1.0;
        mapConstraints.anchor = GridBagConstraints.FIRST_LINE_START;
        mapConstraints.fill = GridBagConstraints.BOTH;

        JSONArray countriesJSON = (JSONArray) mapJSON.get(JSONConstants.COUNTRIES.toString());
        Iterator iterator = countriesJSON.iterator();

        JSONObject jsonObject;

        while(iterator.hasNext()){
            jsonObject = (JSONObject) iterator.next();
            JSONObject jsonGridBag = (JSONObject) jsonObject.get(JSONConstants.GRID_BAG.toString());
            String countryName = (String) jsonObject.get(JSONConstants.COUNTRY_NAME.toString());
            String gridx = (String) jsonGridBag.get(JSONConstants.GRID_X.toString());
            String gridy = (String) jsonGridBag.get(JSONConstants.GRID_Y.toString());
            countryPanel.add(createCountry(countryName, gridx, gridy), mapConstraints);

        }


        // add map to game panel
        gamePanelConstraints.gridx = 0;
        gamePanelConstraints.gridy = 0;
        gamePanelConstraints.gridheight=2;
        gamePanel.add(countryPanel, gamePanelConstraints);

        gamePanelConstraints.gridx = 1;
        gamePanelConstraints.gridy = 0;
        gamePanelConstraints.ipadx = 120;
        gamePanelConstraints.gridheight=1;

        // create a text area for printing game events to a console and added a scrollPane in case of long turns
        textArea = new JTextArea();
        JScrollPane scroll = new JScrollPane(textArea);
        scroll.setPreferredSize(textArea.getPreferredSize());
        textArea.append("It is Player "+ model.getBoard().getPlayers().get(model.getTurnIndex()).getId() +"'s bonus troop phase \n");
        textArea.setEditable(false);
        gamePanel.add(scroll, gamePanelConstraints);


        // Setting Up Dice Rolls Display
        diceJButtons = new ArrayList<>();

        gamePanelConstraints.gridx = 1;
        gamePanelConstraints.gridy = 1;
        JPanel dicePanel = new JPanel();
        gamePanel.add(dicePanel,gamePanelConstraints);


        JButton attackDice1 = new JButton();
        attackDice1.setActionCommand(DiceConstant.A1.toString());
        JButton attackDice2 = new JButton();
        attackDice1.setActionCommand(DiceConstant.A2.toString());
        JButton attackDice3 = new JButton();
        attackDice1.setActionCommand(DiceConstant.A3.toString());
        JButton defendDice1 = new JButton();
        attackDice1.setActionCommand(DiceConstant.D1.toString());
        JButton defendDice2 = new JButton();
        attackDice1.setActionCommand(DiceConstant.D2.toString());

        diceJButtons.add(attackDice1);
        diceJButtons.add(attackDice2);
        diceJButtons.add(attackDice3);
        diceJButtons.add(defendDice1);
        diceJButtons.add(defendDice2);

        dicePanel.setLayout(new GridLayout(4,2));
        dicePanel.add(new JLabel(DiceConstant.ALABEL.toString()));
        dicePanel.add(new JLabel(DiceConstant.DLABEL.toString()));


        dicePanel.add(attackDice1);
        dicePanel.add(defendDice1);
        dicePanel.add(attackDice2);
        dicePanel.add(defendDice2);
        dicePanel.add(attackDice3);

        // Setting up Dice Icons
        diceIcons = new ArrayList<>();

        try{
            for (int i = 1; i< 7;i++) {
                InputStream inputStream = this.getClass().getResourceAsStream(i+ ".png");
                diceIcons.add(ImageIO.read(inputStream));

            }

        }catch(Exception e){
            System.out.println(e);
        }

        frameConstraint.gridx = 0;
        frameConstraint.gridy = 0;
        add(gamePanel, frameConstraint);

        // Creates the control panel at the bottom of the GUI
        JPanel panel = new JPanel(new GridBagLayout());
        frameConstraint.gridx = 0;
        frameConstraint.gridy = 1;
        GridBagConstraints controlPanelConstraints = new GridBagConstraints();
        controlPanelConstraints.weighty = 1.0;
        controlPanelConstraints.weightx = 1.0;
        controlPanelConstraints.anchor = GridBagConstraints.FIRST_LINE_START;
        controlPanelConstraints.fill = GridBagConstraints.BOTH;

        controlPanelConstraints.gridx = 2;
        controlPanelConstraints.gridy = 1;
        controlPanelConstraints.gridwidth = 1;
        reinforce = new JButton(ButtonText.REINFORCE.toString());
        reinforce.setEnabled(false);
        panel.add(reinforce, controlPanelConstraints);

        controlPanelConstraints.gridx = 1;
        controlPanelConstraints.gridy = 1;
        attack = new JButton(ButtonText.ATTACK.toString());
        attack.setEnabled(false);
        panel.add(attack, controlPanelConstraints);

        controlPanelConstraints.gridx = 3;
        controlPanelConstraints.gridy = 1;
        endPhase = new JButton(ButtonText.ENDTURN.toString());
        endPhase.setEnabled(false);
        panel.add(endPhase, controlPanelConstraints);

        controlPanelConstraints.gridx = 0;
        controlPanelConstraints.gridy = 1;
        placeTroops = new JButton(ButtonText.PLACETROOPS.toString());
        placeTroops.setEnabled(true);
        panel.add(placeTroops, controlPanelConstraints);

        add(panel, frameConstraint);

        //creating menu bar for the saving stuff
        JMenu menu = new JMenu(ButtonText.OPTIONS.toString());
        JMenuBar bar = new JMenuBar();
        JMenuItem save = new JMenuItem(ButtonCommand.SAVE.toString());

        menu.add(save);
        bar.add(menu);

        save.addActionListener(riskController);
        save.setActionCommand(ButtonCommand.SAVE.toString());


        // Add bottom Game Buttons to the controller
        attack.addActionListener(riskController);
        attack.setActionCommand(ButtonCommand.ATTACK.toString());

        endPhase.addActionListener(riskController);
        endPhase.setActionCommand(ButtonCommand.ENDPHASE.toString());

        reinforce.addActionListener(riskController);
        reinforce.setActionCommand(ButtonCommand.REINFORCE.toString());

        placeTroops.addActionListener(riskController);
        placeTroops.setActionCommand(ButtonCommand.PLACETROOPS.toString());

        //Starting the first turn of the game

        // Add all country J Buttons to the controller
        for (CountryButton cb : getCountryButtons().values()){
            cb.addActionListener(riskController);
            cb.setActionCommand(ButtonCommand.COUNTRY.toString());
        }

        setDefaultCloseOperation(DISPOSE_ON_CLOSE);
        setVisible(true);
        setExtendedState(JFrame.MAXIMIZED_BOTH);
        setResizable(false);
        setJMenuBar(bar);

    }

    /**
     * General method for creating a country on the map from the JSON file
     * @author Albara'a
     * @param countryName The country name
     * @param x gridx constraint
     * @param y gridy constraint
     * @return A CountryButton that can be added to the country panel
     */
    public CountryButton createCountry(String countryName, String x, String y ){
        mapConstraints.gridx = Integer.parseInt(x);
        mapConstraints.gridy = Integer.parseInt(y);
        CountryButton country = new CountryButton(countries.get(countryName));
        countryButtons.put(country.getName(), country);
        country.setEnabled(false);

        return country;
    }

    /**
     * A popup box that asks the user for the number of human and AI players in the game
     * @author Jason
     * @return A list containing the number of human and ai players
     */
    private int[] invokePlayerPopup() {
        JComboBox playerComboBox = new JComboBox(PlayGame.PLAYERS.getArray());
        JComboBox aiComboBox = new JComboBox(PlayGame.AI_PLAYERS.getArray());
        JPanel panel = createPopupPanel(playerComboBox, aiComboBox);

        int numPlayers = JOptionPane.showOptionDialog(this, panel, PlayGame.TITLE.toString(), JOptionPane.NO_OPTION, JOptionPane.QUESTION_MESSAGE,
                null, PlayGame.OK_CANCEL_OPTION.getArray(), PlayGame.OK_CANCEL_OPTION.getArray()[0]);

        String aiResult = aiComboBox.getSelectedItem().toString();
        String humanPlayerResult = playerComboBox.getSelectedItem().toString();
        int totalPlayer = Integer.parseInt(humanPlayerResult) + Integer.parseInt(aiResult);
        while (numPlayers != 0 || totalPlayer > MAX_PLAYERS || totalPlayer == MIN_PLAYERS) {
            numPlayers = JOptionPane.showOptionDialog(null, panel, PlayGame.TITLE.toString(), JOptionPane.NO_OPTION, JOptionPane.QUESTION_MESSAGE,
                    null, PlayGame.OK_CANCEL_OPTION.getArray(), PlayGame.OK_CANCEL_OPTION.getArray()[0]);
            totalPlayer = Integer.parseInt(playerComboBox.getSelectedItem().toString()) + Integer.parseInt(aiComboBox.getSelectedItem().toString());
        }
        humanPlayerResult = playerComboBox.getSelectedItem().toString();
        aiResult = aiComboBox.getSelectedItem().toString();
        int players[] = {Integer.parseInt(humanPlayerResult), Integer.parseInt(aiResult)};
        return players;
    }

    /**
     * Creates a JPanel for the player popup
     * @param playerComboBox The human player dropdown
     * @param aiComboBox The AI player dropdown
     * @return JPanel with human player and AI player dropdown
     */
    private JPanel createPopupPanel(JComboBox playerComboBox, JComboBox aiComboBox) {
        JPanel panel = new JPanel();
        JLabel playerLabel = new JLabel(PlayGame.LABEL.toString());
        JLabel aiLabel = new JLabel(PlayGame.AI_LABEL.toString());
        playerComboBox.setSelectedIndex(0);
        aiComboBox.setSelectedIndex(0);
        panel.add(playerLabel);
        panel.add(playerComboBox);
        panel.add(aiLabel);
        panel.add(aiComboBox);
        return panel;
    }

    /**
     * A general game popup
     * @author Harjap
     * @param popupMessage A message shown on the popup
     * @return True if yes option clicked and false otherwise
     */
    public boolean generalGameInitPopup(String popupMessage){
        int result = JOptionPane.showOptionDialog(this,popupMessage,"Game Initialization",
                JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE, null, null, null);

        // No -> 1
        // Yes -> 0
        if (result == 1){
            return false;
        }
        return true;
    }

    /**
     * Opens a fileChooser and allows user to select custom maps
     * @author Jason
     * @return A File object which is the JSON file
     */
    public File chooseFile() {
        //Create a file chooser
        final JFileChooser fc = new JFileChooser();
        fc.setFileFilter(new FileNameExtensionFilter(FileChooser.JSON_DESCRIPTION.toString(), FileChooser.JSON_DESCRIPTION.toString()));
        fc.setFileFilter(new FileNameExtensionFilter(FileChooser.TXT_DESCRIPTION.toString(), FileChooser.TXT_TYPE.toString()));
        int selectedFile = fc.showOpenDialog(this);
        while (selectedFile != JFileChooser.APPROVE_OPTION) {
            selectedFile = fc.showOpenDialog(this);
        }
        return fc.getSelectedFile();
    }

    public RiskModel loadFromFile(File file){
        try(FileInputStream fis = new FileInputStream(file)){
            ObjectInputStream ois = new ObjectInputStream(fis);
            RiskModel model = (RiskModel) ois.readObject();
            ois.close();
            return model;
        }catch (EOFException e){
            e.printStackTrace();

        }catch (IOException | ClassNotFoundException e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * Parses a JSON file into a JSON object
     * @author Jason
     * @return A JSON object of the custom map
     */
    private JSONObject parseFile() {
        JSONParser parser = new JSONParser();
        JSONObject jsonObject = null;
        try {
            Object obj = parser.parse(new FileReader(chooseFile()));

            // A JSON object. Key value pairs are unordered. JSONObject supports java.util.Map interface.
            jsonObject = (JSONObject) obj;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return jsonObject;
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
     */
    public void getAttackingTroopCount(Country attackingCountry) {
        String[] options = {"OK", "CANCEL"};
        int troopCount = attackingCountry.getArmySize()-1;
        Integer[] troopList = buildTroopDropdownList(troopCount);
        JPanel panel = new JPanel();
        JLabel label = new JLabel("Select the number of troops to attack with: ");
        JComboBox comboBox = new JComboBox(troopList);
        comboBox.setSelectedIndex(0);
        panel.add(label);
        panel.add(comboBox);
        int selectionObject = JOptionPane.showOptionDialog(this, panel, "Choose Troops", JOptionPane.NO_OPTION, JOptionPane.QUESTION_MESSAGE, null, options, options[0]);
        String result;
        if (selectionObject == 0) {
            result = comboBox.getSelectedItem().toString();
            model.setMovedTroops(Integer.parseInt(result));
        } else {
            model.setMovedTroops(0);
        }
    }

    /**
     * Creates a dialog box with a dropdown list asking the user for the number of bonus troops that can be moved
     * @author Jason
     * @param troopCount The number of bonus troops available to move
     */
    public int getBonusTroopCount(int troopCount) {
        String[] options = {"OK"};
        Integer[] troopList = buildTroopDropdownList(troopCount);
        JPanel panel = new JPanel();
        JLabel label = new JLabel("Select the number of troops to move: ");
        JComboBox comboBox = new JComboBox(troopList);
        comboBox.setSelectedIndex(0);
        panel.add(label);
        panel.add(comboBox);
        int selectionObject = JOptionPane.showOptionDialog(this, panel, "Choose Troops", JOptionPane.NO_OPTION, JOptionPane.QUESTION_MESSAGE, null, options, options[0]);
        while (selectionObject != 0) {
            selectionObject = JOptionPane.showOptionDialog(this, panel, "Choose Troops", JOptionPane.NO_OPTION, JOptionPane.QUESTION_MESSAGE, null, options, options[0]);
        }
        int result = Integer.parseInt(comboBox.getSelectedItem().toString());
        model.setMovedTroops(result);
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
     * Sets the dice icon corresponding to the attacker and defending dice rolls
     * @author Harjap
     * @param diceJButton The JButton the icon will be placed on
     * @param diceNum The dice value
     */
    public void setDiceIcon(JButton diceJButton, int diceNum){
        ImageIcon icon = new ImageIcon(diceIcons.get(diceNum-1));
        diceJButton.setIcon(icon);
    }

    /**
     * shows all the countries that the player can add bonus troops to
     */
    @Override
    public void handleShowTroopPlacementCountry() {
        // get all the Country Buttons that the current player owns
        ArrayList<CountryButton> countryButtons = convertCountryToCountryButtons(model.getAttackingPlayer().getCountriesOwned());
        countryButtons.forEach(cb -> cb.setEnabled(true));
        countryButtons.forEach(cb -> cb.setBorder(BorderFactory.createLineBorder(Color.YELLOW, THICKNESS)));

        placeTroops.setEnabled(false);
        attack.setEnabled(false);
        endPhase.setEnabled(false);
        reinforce.setEnabled(false);
    }

    /**
     * updates the text area with the number of bonus troops places and updates the country button text
     * @author Shashaank
     * @param bte BonusTroopsEvent contains information about the bonus troop phase
     */
    @Override
    public void handleTroopPlaced(BonusTroopEvent bte) {
        int turnIndex = bte.getModel().getTurnIndex();
        Player player = bte.getModel().getBoard().getPlayers().get(turnIndex);
        int selectedTroops;
        if (player instanceof HumanPlayer) {
            selectedTroops = getBonusTroopCount(bte.getRemainingTroops());
            textArea.append(selectedTroops + " Troops were placed in " + bte.getBonusCountry().getName() + "\n");
        } else {
            selectedTroops = bte.getRemainingTroops();
            textArea.setText("");
        }
        bte.getBonusCountry().setArmySize(bte.getBonusCountry().getArmySize() + selectedTroops);
        countryButtons.get(bte.getBonusCountry().getName()).update();
        placeTroops.setEnabled(true);

        ArrayList<CountryButton> countryButtons = convertCountryToCountryButtons(model.getAttackingPlayer().getCountriesOwned());
        countryButtons.forEach(cb -> cb.setEnabled(false));
        countryButtons.forEach(cb -> cb.setBorder(null));
    }

    /**
     * Updates text area to show that all bonus troops have been allocated
     * @author Shashaank
     */
    @Override
    public void troopBonusComplete() {
        textArea.append("All troops have been allocated move to the next phase");
        placeTroops.setEnabled(false);
        attack.setEnabled(false);
        endPhase.setEnabled(true);
        reinforce.setEnabled(false);

    }

    /**
     * Updates the view in case the attack is cancelled by user
     * @author Jason
     * @param attackingCountry The attacking country of the user
     */
    public void handleAttackCancelled(Country attackingCountry) {
        ArrayList<CountryButton> defendingCountries = convertCountryToCountryButtons(attackingCountry.getPossibleAttacks());
        defendingCountries.forEach(countryButton -> countryButton.setEnabled(false));
        defendingCountries.forEach(countryButton -> countryButton.setBorder(null));
        attack.setEnabled(true);
        endPhase.setEnabled(true);
    }

    /**
     * Enables the attacking players CountryButton so that they may choose a country to attack from
     * @author Jason
     */
    public void handleShowAttackingCountry() {
        // get all the Country Buttons that the current player owns
        ArrayList<CountryButton> countryButtons = convertCountryToCountryButtons(model.getAttackingPlayer().getCountriesOwned());

        for (CountryButton cb : countryButtons){
            if (cb.getCountry().getPossibleAttacks().size() != 0 && cb.getCountry().getArmySize() >= MIN_ARMY_SIZE) {
                cb.setEnabled(true);
                cb.setBorder(BorderFactory.createLineBorder(Color.YELLOW, THICKNESS));
            }
        }
        attack.setEnabled(false);
        endPhase.setEnabled(false);
    }


    /**
     * Highlights all the countries the attacking player can attack from by creating a border around it
     * @author Jason
     * @param attackingCountry The attacking country
     * @return The number of troops that the player chose to use in attacking the country
     */
    @Override
    public void handleShowDefendingCountry(Country attackingCountry) {
        // converts all countries that the attacking country can attack into their respective country button instance in the view and stores in a list
        ArrayList<CountryButton> defendingCountries = convertCountryToCountryButtons(attackingCountry.getPossibleAttacks());
        defendingCountries.forEach(countryButton -> countryButton.setEnabled(true)); // enable all possible countries to attack
        defendingCountries.forEach(countryButton -> countryButton.setBorder(BorderFactory.createLineBorder(Color.yellow, 3)));
        // disables all attacking countries from being pressed
        ArrayList<CountryButton> countryButtons = convertCountryToCountryButtons(model.getAttackingPlayer().getCountriesOwned());
        countryButtons.forEach(countryButton -> countryButton.setEnabled(false));
        countryButtons.forEach(countryButton -> countryButton.setBorder(null));

        // asks for number of troops to attack with
        getAttackingTroopCount(attackingCountry); // number of troops to attack with
    }

    /**
     * Handles the behaviour behind executing an attack by calling on the respective method in the model.
     * This method also deals with setting up a continuous attack phase in case the user would like to attack again.
     * @author Jason
     * @param attackingCountry The country the user is attacking from
     */
    public void handleCountryAttack(Country attackingCountry) {
        // disable all defending countries of the attacking country
        ArrayList<CountryButton> countryButtons = convertCountryToCountryButtons(attackingCountry.getAdjacentCountries());
        countryButtons.forEach(countryButton -> countryButton.setEnabled(false));
        countryButtons.forEach(countryButton -> countryButton.setBorder(null));
        endPhase.setEnabled(true);
        attack.setEnabled(true);
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

        JOptionPane.showMessageDialog(null, textArea.getText());
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
            textArea.append("You attacked from " + cle.getAttackingCountry().getName() + " and failed to conquer "+ cle.getDefendingCountry().getName()+"\n");
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
     * Handles the player eliminated event and creates a OptionPane to let user know who got eliminated
     * @author Albara'a
     * @param playerID The player ID of the player who was eliminated
     */
    public void handlePlayerEliminated(int playerID) {
        JOptionPane.showMessageDialog(this, "Player" + playerID + "was eliminated, sorry to see you go :(" );
    }

    /**
     * Updates the view when a reinforce event is cancelled
     * @author Jason
     * @param reinforceCountry The country to reinforce from
     */
    public void handleReinforceCancelled(Country reinforceCountry) {
        ArrayList<CountryButton> countryButtons = convertCountryToCountryButtons(model.getReinforceCountries());
        countryButtons.forEach(countryButton -> countryButton.setEnabled(false));
        countryButtons.forEach(countryButton -> countryButton.setBorder(null));

        ArrayList<CountryButton> reinforceCountries = convertCountryToCountryButtons(model.getConnectedCountries(reinforceCountry));
        reinforceCountries.forEach(countryButton -> countryButton.setEnabled(false));
        reinforceCountries.forEach(countryButton -> countryButton.setBorder(null));
        reinforce.setEnabled(true);
        endPhase.setEnabled(true);
    }

    /**
     * Update view to show all the countries that the player can reinforce from
     * @author Jason
     */
    @Override
    public void handleShowReinforceCountry() {
        ArrayList<CountryButton> reinforceCountries = convertCountryToCountryButtons(model.getReinforceCountries());
        for (CountryButton cb : reinforceCountries){
            if (cb.getCountry().getArmySize() >= MIN_ARMY_SIZE) {
                cb.setEnabled(true);
                cb.setBorder(BorderFactory.createLineBorder(Color.yellow, THICKNESS));
            }
        }
        endPhase.setEnabled(false);
        reinforce.setEnabled(false);
    }

    /**
     * Update the view to show all countries the player can reinforce into
     * @author Jason
     * @param reinforceCountry The country to reinforce from
     */
    @Override
    public void handleShowReinforceAdjacents(Country reinforceCountry) {
        // disables all countries you can reinforce from from being pressed
        ArrayList<CountryButton> countryButtons = convertCountryToCountryButtons(model.getReinforceCountries());
        countryButtons.forEach(countryButton -> countryButton.setEnabled(false));
        countryButtons.forEach(countryButton -> countryButton.setBorder(null));

        // converts all countries that the reinforce country can reinforce into their respective country button instance in the view and stores in a list
        ArrayList<CountryButton> reinforceCountries = convertCountryToCountryButtons(model.getConnectedCountries(reinforceCountry));
        reinforceCountries.forEach(countryButton -> {
            if (countryButton.getCountry().equals(reinforceCountry)) {
                countryButton.setEnabled(false);
                countryButton.setBorder(BorderFactory.createLineBorder(null));
            } else {
                countryButton.setEnabled(true);
                countryButton.setBorder(BorderFactory.createLineBorder(Color.yellow, THICKNESS));
            }
        });

        // asks for number of troops to attack with
        getAttackingTroopCount(reinforceCountry); // number of troops to reinforce with
    }

    /**
     * Updates the view to show all the adjacency path that a player can use to reinforce countries
     * @param reinforceCountry The country being reinforced from
     */
    @Override
    public void handleReinforce(Country reinforceCountry) {
        // disable all countries that reinforce country can reinforce
        ArrayList<CountryButton> countryButtons = convertCountryToCountryButtons(model.getConnectedCountries(reinforceCountry));
        countryButtons.forEach(countryButton -> countryButton.setEnabled(false));
        countryButtons.forEach(countryButton -> countryButton.setBorder(null));

        endPhase.setEnabled(true);
        reinforce.setEnabled(true);
    }

    /**
     * Update the text area with the result of the reinforcement
     * @param rre ReinforceResultEvent contains information about a reinforcement event
     */
    public void handleReinforceResultEvent(ReinforceResultEvent rre) {
        textArea.append(rre.getReinforceCountry().getName() + " has moved " + rre.getReinforceArmy() + " troops to " + rre.getCountryToReinforce().getName() + "\n");
    }

    /**
     * Updates the buttons afters a reinforce event
     * @param re ReinforceEvent contains information about a reinforcement event
     */
    public void handleReinforceEvent(ReinforceEvent re) {
        Country reinforceCountry = re.getReinforceCountry();
        Country countryToReinforce = re.getCountryToReinforce();

        for (CountryButton cb : countryButtons.values()) {
            if (cb.getName().equals(reinforceCountry.getName())){
                countryButtons.get(reinforceCountry.getName()).update();
            }
            else if(cb.getName().equals(countryToReinforce.getName())){
                countryButtons.get(countryToReinforce.getName()).update();
            }
        }
    }

    /**
     * Updates the text area with bonus event result
     * @param playerID the player id who just added bonus troop
     */
    public void handleEndBonus(int playerID) {
        textArea.setText("");
        textArea.append("It is Player "+playerID+"'s attack phase\n");
        attack.setEnabled(true);
        endPhase.setEnabled(true);
        reinforce.setEnabled(false);
    }

    /**
     * Updates the view when the attack phase is over
     * @param playerID the player id who ended their attack phase
     */
    public void handleEndAttack(int playerID) {
        textArea.setText("");
        textArea.append("It is Player "+playerID+"'s reinforce phase\n");
        attack.setEnabled(false);
        endPhase.setEnabled(true);
        reinforce.setEnabled(true);
    }

    /**
     * Handles an EndTurnEvent by printing the information to the text area console
     * @author Albara'a
     * @param playerId The next player's ID
     */
    public void handleEndTurn(int playerId){
        // disable all attacking countries of current player
        ArrayList<CountryButton> countryButtons = convertCountryToCountryButtons(model.getEndTurnPlayer().getCountriesOwned());
        countryButtons.forEach(countryButton -> countryButton.setEnabled(false));
        textArea.setText("");
        textArea.append("It is Player "+playerId+"'s bonus troop phase\n");
        placeTroops.setEnabled(true);
        reinforce.setEnabled(false);
        attack.setEnabled(false);
        endPhase.setEnabled(false);
        int turnIndex = model.getTurnIndex();
        Player player = model.getBoard().getPlayers().get(turnIndex);
        if (player instanceof AI) {
            AI ai = (AI) player;
            ai.playTurn();
        }
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

    public HashMap<String, CountryButton> getCountryButtons(){
        return countryButtons;
    }

    public static void main(String[] args) {
        new RiskFrame();
    }
}
