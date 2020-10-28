import java.util.ArrayList;
import java.util.EventObject;

public class TroopUpdateEvent extends EventObject {

    private ArrayList<Country> countries;
    /**
     * Constructs a prototypical Event.
     *
     * @param riskModel The object on which the Event initially occurred.
     * @throws IllegalArgumentException if source is null.
     */
    public TroopUpdateEvent(RiskGame riskModel, ArrayList<Country> countries ){
        super(riskModel);
        this.countries=countries;

    }

    public ArrayList<Country> getCountries() {
        return countries;
    }
}
