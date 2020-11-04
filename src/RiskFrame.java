import javax.swing.*;
import java.awt.*;

public class RiskFrame extends JFrame {
    public RiskFrame() {
        super("Risk!");
        setLayout(new GridBagLayout());
        //Instantiating the model
        RiskModel model = new RiskModel();
        model.playGame();

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
        controlPanelConstraints.anchor = GridBagConstraints.LAST_LINE_END;
        JButton reinforce = new JButton("REINFORCE");
        reinforce.setEnabled(false);
        panel.add(reinforce, controlPanelConstraints);

        controlPanelConstraints.gridx = 1;
        controlPanelConstraints.gridy = 1;
        controlPanelConstraints.anchor = GridBagConstraints.LAST_LINE_END;
        JButton attack = new JButton("ATTACK");
        attack.setEnabled(true);
        panel.add(attack, controlPanelConstraints);

        controlPanelConstraints.gridx = 2;
        controlPanelConstraints.gridy = 1;
        controlPanelConstraints.anchor = GridBagConstraints.LAST_LINE_END;
        JButton endturn = new JButton("END TURN");
        endturn.setEnabled(true);
        panel.add(endturn, controlPanelConstraints);

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
        attack.setActionCommand("attack");

        riskController.addActionListener(endturn);
        endturn.setActionCommand("endturn");
        //riskController.addActionListener(reinforce);


        // Add all country J Buttons to the controller
        for (CountryButton cb : riskView.getCountryButtons().values()){
            riskController.addActionListener(cb);
            cb.setActionCommand("country");
        }


        setDefaultCloseOperation(DISPOSE_ON_CLOSE);
        setVisible(true);
        setExtendedState(JFrame.MAXIMIZED_BOTH);
        setResizable(false);

    }

    public static void main(String[] args) {
        new RiskFrame();
    }
}
