import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;

/**
 * The controller class handles all sort of events raised by the CountryButton countries
 * @author Jason, Harjap
 */
public class RiskController implements ActionListener{
    private RiskModel riskModel;
    private RiskView riskView;
    private Country attackingCountry;
    private Country defendingCountry;
    private JButton attack;
    private int troopCount;
    private enum attackState {
        SHOW_PLAYER_COUNTRIES, SHOW_DEFENDING_COUNTRIES, COMMENCE_ATTACK
    }
    private attackState currentState;

    public RiskController(RiskModel riskModel, RiskView riskView){
        this.riskModel = riskModel;
        this.riskView = riskView;
        this.currentState = null;
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if(e.getActionCommand().equals("country")) {
            CountryButton countryButton = (CountryButton) e.getSource();
            // Dealing with countries being clicked
            //identify what turn it is
            if (currentState == attackState.SHOW_DEFENDING_COUNTRIES) {
                attackingCountry = countryButton.getCountry();
                troopCount = riskView.showDefendingCountries(e);
                currentState = attackState.COMMENCE_ATTACK;
            } else if (currentState == attackState.COMMENCE_ATTACK) {
                defendingCountry = countryButton.getCountry();
                riskModel.attack(attackingCountry, defendingCountry, troopCount);
                riskView.performAttack(attackingCountry);
                currentState = attackState.SHOW_DEFENDING_COUNTRIES;
            }
        }else if (e.getActionCommand().equals("endturn")){
            if (currentState == attackState.SHOW_DEFENDING_COUNTRIES || currentState == null) {
                // disable all attacking countries of current player
                ArrayList<CountryButton> countryButtons = riskView.convertCountryToCountryButtons(riskModel.getAttackingPlayer().getCountriesOwned());
                countryButtons.forEach(countryButton -> countryButton.setEnabled(false));

                // moves to next player
                riskModel.endTurnPhase(riskModel.getBoard().getPlayers().get(riskModel.getTurnIndex()).getId());
                attack.setEnabled(true);
            }
        } else if (e.getActionCommand().equals("attack")){
            // show all countries that we can attack with
            // this button should get disabled after until the attacking phase has been completed
            currentState = attackState.SHOW_PLAYER_COUNTRIES;
            attack = (JButton) e.getSource();
            attack.setEnabled(false);
            if (currentState == attackState.SHOW_PLAYER_COUNTRIES) {
                riskView.showAttackingCountries();
                currentState = attackState.SHOW_DEFENDING_COUNTRIES;
            }
        }

    }

    /**
     * Adds an actionListener to a JButton instance
     * @param component
     */
    public void addActionListener(JButton component){
        component.addActionListener(this);
    }

}
