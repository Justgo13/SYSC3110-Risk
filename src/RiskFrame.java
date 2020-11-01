import javax.swing.*;
import java.awt.*;

public class RiskFrame extends JFrame {
    public RiskFrame() {
        super("Risk!");
        //Instantiating the model
        RiskGame model = new RiskGame();
        model.playGame();

        // Creates the control panel at the bottom of the GUI
        JPanel panel = new JPanel(new GridBagLayout());
        GridBagConstraints controlPanelConstraints = new GridBagConstraints();
        controlPanelConstraints.weighty = 1.0;
        controlPanelConstraints.weightx = 1.0;
        controlPanelConstraints.anchor = GridBagConstraints.FIRST_LINE_START;
        controlPanelConstraints.fill = GridBagConstraints.BOTH;

        controlPanelConstraints.gridx = 0;
        controlPanelConstraints.gridy = 0;
        controlPanelConstraints.gridwidth = 3;
        controlPanelConstraints.gridheight = 1;
        controlPanelConstraints.anchor = GridBagConstraints.FIRST_LINE_END;
        JTextArea console = new JTextArea();
        console.setEditable(false);
        panel.add(console, controlPanelConstraints);

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
        endturn.setEnabled(false);
        panel.add(endturn, controlPanelConstraints);


        // Creates the VIEW
        RiskView riskView = new RiskView(model);
        model.addRiskView(riskView);

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


        add(riskView, BorderLayout.CENTER);
        add(panel, BorderLayout.PAGE_END);

        setDefaultCloseOperation(DISPOSE_ON_CLOSE);
        setVisible(true);
        setSize(1920, 960);
        setLocationRelativeTo(null);



    }

    public static void main(String[] args) {
        new RiskFrame();
    }
}
