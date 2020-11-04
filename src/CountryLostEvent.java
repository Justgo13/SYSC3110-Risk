import java.util.EventObject;

public class CountryLostEvent extends EventObject {
    private Country defendingCountry;
    private int attackingPlayerIndex;
    public CountryLostEvent(RiskGame riskModel, Country defendingCountry, int attackingPlayerIndex) {
        super(riskModel);
        this.defendingCountry = defendingCountry;
        this.attackingPlayerIndex = attackingPlayerIndex;
    }

    public Country getDefendingCountry() {
        return defendingCountry;
    }

    public int getAttackingPlayerIndex() {
        return attackingPlayerIndex;
    }
}
