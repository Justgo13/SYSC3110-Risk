import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class RiskController implements ActionListener{
    private RiskGame riskModel;
    private RiskView riskView;

    public RiskController(RiskGame riskModel, RiskView riskView){
        this.riskModel = riskModel;
        this.riskView = riskView;

    }

    @Override
    public void actionPerformed(ActionEvent e) {

        if(e.getActionCommand().equals("country")){
            // Dealing with countries being clicked
            //identify what turn it is
        } else if (e.getActionCommand().equals("attack")){
            // show all countries that we can attack with
            // this button should get disabled after until the attacking phase has been completed
        }

    }
}
