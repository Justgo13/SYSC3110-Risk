import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.HashMap;

public class RiskController implements ActionListener{
    private RiskGame riskModel;
    private RiskView riskView;
    private int attackState;

    public RiskController(RiskGame riskModel, RiskView riskView){
        this.riskModel = riskModel;
        this.riskView = riskView;

    }

    public ArrayList<CountryButton> convertCountryToCountryButtons(ArrayList<Country> countries){

        int turnIndex = riskModel.getTurnIndex();
        ArrayList<Player> players = riskModel.getBoard().getPlayers();

        // player who is attacking
        Player currentPlayer = players.get(turnIndex);

        // build an arraylist of country names
        ArrayList<String> countryNames = new ArrayList<String>();
        for (Country c : currentPlayer.getCountriesOwned()){
            countryNames.add(c.getName());
        }

        // get hashmap from view that holds relationship between country name and country Button
        HashMap<String, CountryButton> countryButtonHashMap = riskView.getCountryButtons();

        // create empty arraylist for return
        ArrayList<CountryButton> countryButtons = new ArrayList<CountryButton>();

        for (String s: countryNames){
            countryButtons.add(countryButtonHashMap.get(s));
        }

        return countryButtons;


    }

    @Override
    public void actionPerformed(ActionEvent e) {

        if(e.getActionCommand().equals("country")) {
            // Dealing with countries being clicked
            //identify what turn it is

        }else if (e.getActionCommand().equals("endturn")){

        } else if (e.getActionCommand().equals("attack")){
            // show all countries that we can attack with
            // this button should get disabled after until the attacking phase has been completed

            if (attackState == 0){
                // disables the attack button
                JButton attack = (JButton) e.getSource();
                attack.setEnabled(false);

                // enable all country buttons that this player owns

                int turnIndex = riskModel.getTurnIndex();
                ArrayList<Player> players = riskModel.getBoard().getPlayers();

                // player who is attacking
                Player currentPlayer = players.get(turnIndex);

                // get all the Country Buttons that the current player owns
                ArrayList<CountryButton> countryButtons = convertCountryToCountryButtons(currentPlayer.getCountriesOwned());


                for (CountryButton cb : countryButtons){
                    if (cb.getCountry().getPossibleAttacks().size() != 0) {
                        cb.setEnabled(true);
                    }
                }
                attackState++;

            }
        }

    }

    public void addActionListener(JButton component){
        component.addActionListener(this);

    }

}
