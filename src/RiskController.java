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

    public RiskController(RiskModel riskModel, RiskView riskView){
        this.riskModel = riskModel;
        this.riskView = riskView;
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if(e.getActionCommand().equals(ButtonCommand.COUNTRY.toString())) {
            CountryButton countryButton = (CountryButton) e.getSource();
            // Dealing with countries being clicked
            //identify what turn it is
            if (riskModel.getState() == AttackState.SHOW_DEFENDING_COUNTRIES) {
                attackingCountry = countryButton.getCountry();
                troopCount = riskView.showDefendingCountries(e);
                riskModel.updateNextState();
            } else if (riskModel.getState() == AttackState.COMMENCE_ATTACK) {
                defendingCountry = countryButton.getCountry();
                riskModel.attack(attackingCountry, defendingCountry, troopCount);
                riskView.performAttack(attackingCountry);
                riskModel.updatePrevState();
            }
        } else if (e.getActionCommand().equals(ButtonCommand.ENDTURN.toString())){
            if (riskModel.getState() == AttackState.SHOW_DEFENDING_COUNTRIES || riskModel.getState() == null) {
                // disable all attacking countries of current player
                ArrayList<CountryButton> countryButtons = riskView.convertCountryToCountryButtons(riskModel.getAttackingPlayer().getCountriesOwned());
                countryButtons.forEach(countryButton -> countryButton.setEnabled(false));

                // moves to next player
                riskModel.endTurnPhase(riskModel.getBoard().getPlayers().get(riskModel.getTurnIndex()).getId());
                attack.setEnabled(true);
            }
        } else if (e.getActionCommand().equals(ButtonCommand.ATTACK.toString())){
            // show all countries that we can attack with
            // this button should get disabled after until the attacking phase has been completed
            riskModel.setState(AttackState.SHOW_PLAYER_COUNTRIES);
            attack = (JButton) e.getSource();
            attack.setEnabled(false);
            riskView.showAttackingCountries();
            riskModel.updateNextState();
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
