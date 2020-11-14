import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

/**
 * The controller class handles all sort of events raised by the CountryButton countries
 * @author Jason, Harjap
 */
public class RiskController implements ActionListener{
    private RiskModel riskModel;

    public RiskController(RiskModel riskModel){
        this.riskModel = riskModel;
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if(e.getActionCommand().equals(ButtonCommand.COUNTRY.toString())) {
            CountryButton countryButton = (CountryButton) e.getSource();
            riskModel.countryClicked(countryButton.getCountry());
        } else if (e.getActionCommand().equals(ButtonCommand.ENDTURN.toString())){
            riskModel.endTurnPhase();
        } else if (e.getActionCommand().equals(ButtonCommand.ATTACK.toString())){
            riskModel.attackClicked();
        }
    }
}
