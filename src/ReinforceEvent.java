import java.util.EventObject;

public class ReinforceEvent extends EventObject {
    private Country reinforceCountry;
    private Country countryToReinforce;
    public ReinforceEvent(RiskModel riskModel, Country reinforceCountry, Country countryToReinforce) {
        super(riskModel);
        this.reinforceCountry = reinforceCountry;
        this.countryToReinforce = countryToReinforce;
    }

    public Country getReinforceCountry() {
        return reinforceCountry;
    }

    public Country getCountryToReinforce() {
        return countryToReinforce;
    }
}
