import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

/**
 * The controller class handles all sort of events raised by the CountryButton countries
 * @author Jason, Harjap
 */
public class RiskController implements ActionListener{
    private static final String FILE_NAME = "serializable.txt";
    private RiskModel riskModel;

    public RiskController(RiskModel riskModel){
        this.riskModel = riskModel;
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if(e.getActionCommand().equals(ButtonCommand.COUNTRY.toString())) {
            CountryButton countryButton = (CountryButton) e.getSource();
            riskModel.countryClicked(countryButton.getCountry());
        } else if (e.getActionCommand().equals(ButtonCommand.ENDPHASE.toString())){
            riskModel.endPhaseClicked();
        } else if (e.getActionCommand().equals(ButtonCommand.ATTACK.toString())){
            riskModel.attackClicked();
        } else if (e.getActionCommand().equals(ButtonCommand.REINFORCE.toString())) {
            riskModel.reinforceClicked();
        } else if (e.getActionCommand().equals(ButtonCommand.PLACETROOPS.toString())){
            riskModel.placeTroopsClicked();
        } else if (e.getActionCommand().equals(ButtonCommand.SAVE.toString())){
            riskModel.saveGame(FILE_NAME);
        }
    }
}
