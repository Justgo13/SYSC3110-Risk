import javax.swing.*;
import java.awt.*;

/**
 * Creates a JFrame for the Risk game containing a button panel and view class
 * @author Jason
 */
public class RiskFrame extends JFrame {
    private RiskModel model;
    public RiskFrame() {
        super(PlayGame.GAMETITLE.toString());
        setLayout(new GridBagLayout());
        // ask for player number
        int numPlayer = invokePlayerPopup();

        //Instantiating the model
        setupModel(numPlayer);

        // Creating a constraint for the entire frame
        GridBagConstraints frameConstraint = new GridBagConstraints();
        frameConstraint.weighty = 1.0;
        frameConstraint .weightx = 1.0;
        frameConstraint .anchor = GridBagConstraints.FIRST_LINE_START;
        frameConstraint .fill = GridBagConstraints.BOTH;

        // Creates the control panel at the bottom of the GUI
        JPanel panel = new JPanel(new GridBagLayout());
        frameConstraint.gridx = 0;
        frameConstraint.gridy = 1;
        GridBagConstraints controlPanelConstraints = new GridBagConstraints();
        controlPanelConstraints.weighty = 1.0;
        controlPanelConstraints.weightx = 1.0;
        controlPanelConstraints.anchor = GridBagConstraints.FIRST_LINE_START;
        controlPanelConstraints.fill = GridBagConstraints.BOTH;

        controlPanelConstraints.gridx = 0;
        controlPanelConstraints.gridy = 1;
        controlPanelConstraints.gridwidth = 1;
        JButton reinforce = new JButton(ButtonText.REINFORCE.toString());
        reinforce.setEnabled(false);
        panel.add(reinforce, controlPanelConstraints);

        controlPanelConstraints.gridx = 1;
        controlPanelConstraints.gridy = 1;
        JButton attack = new JButton(ButtonText.ATTACK.toString());
        attack.setEnabled(true);
        panel.add(attack, controlPanelConstraints);

        controlPanelConstraints.gridx = 2;
        controlPanelConstraints.gridy = 1;
        JButton endTurn = new JButton(ButtonText.ENDTURN.toString());
        endTurn.setEnabled(true);
        panel.add(endTurn, controlPanelConstraints);

        add(panel, frameConstraint);

        frameConstraint.gridx = 0;
        frameConstraint.gridy = 0;

        // Creates the VIEW
        RiskView riskView = new RiskView(model);
        model.addRiskView(riskView);
        add(riskView, frameConstraint);

        // Creates the Controller
        RiskController riskController = new RiskController(model,riskView);

        // Add bottom Game Buttons to the controller
        riskController.addActionListener(attack);
        attack.setActionCommand(ButtonCommand.ATTACK.toString());

        riskController.addActionListener(endTurn);
        endTurn.setActionCommand(ButtonCommand.ENDTURN.toString());
        //riskController.addActionListener(reinforce);


        // Add all country J Buttons to the controller
        for (CountryButton cb : riskView.getCountryButtons().values()){
            riskController.addActionListener(cb);
            cb.setActionCommand(ButtonCommand.COUNTRY.toString());
        }


        setDefaultCloseOperation(DISPOSE_ON_CLOSE);
        setVisible(true);
        setExtendedState(JFrame.MAXIMIZED_BOTH);
        setResizable(false);

    }

    private int invokePlayerPopup() {
        JPanel panel = new JPanel();
        JLabel label = new JLabel(PlayGame.LABEL.toString());
        JComboBox comboBox = new JComboBox(PlayGame.PLAYERS.getArray());
        comboBox.setSelectedIndex(0);
        panel.add(label);
        panel.add(comboBox);
        int numPlayers = JOptionPane.showOptionDialog(null, panel, PlayGame.TITLE.toString(), JOptionPane.NO_OPTION, JOptionPane.QUESTION_MESSAGE,
                null, PlayGame.OK_CANCEL_OPTION.getArray(), PlayGame.OK_CANCEL_OPTION.getArray()[0]);
        String result = PlayGame.DEFAULTPLAYER.toString();
        while (numPlayers != 0) {
            numPlayers = JOptionPane.showOptionDialog(null, panel, PlayGame.TITLE.toString(), JOptionPane.NO_OPTION, JOptionPane.QUESTION_MESSAGE,
                    null, PlayGame.OK_CANCEL_OPTION.getArray(), PlayGame.OK_CANCEL_OPTION.getArray()[0]);
        }
        if (numPlayers == 0) {
            result = comboBox.getSelectedItem().toString();
        }
        return Integer.parseInt(result);
    }

    private void setupModel(int numPlayers) {
        model = new RiskModel();
        model.playGame(numPlayers);
    }

    public static void main(String[] args) {
        new RiskFrame();
    }
}
