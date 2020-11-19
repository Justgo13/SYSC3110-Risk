import java.util.EventObject;

public class ReinforceResultEvent extends EventObject {
    private int reinforceArmy;
    private Country reinforceCountry;
    private Country countryToReinforce;
    public ReinforceResultEvent(RiskModel riskModel, Country reinforceCountry, int reinforceArmy, Country countryToReinforce) {
        super(riskModel);
        this.reinforceCountry = reinforceCountry;
        this.reinforceArmy = reinforceArmy;
        this.countryToReinforce = countryToReinforce;
    }

    public int getReinforceArmy() {
        return reinforceArmy;
    }

    public Country getCountryToReinforce() {
        return countryToReinforce;
    }

    public Country getReinforceCountry() {
        return reinforceCountry;
    }
}
