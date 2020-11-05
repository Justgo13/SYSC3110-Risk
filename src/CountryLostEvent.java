import java.util.EventObject;

/**
 * An event containing information about a country that was seized in an attack
 * @author Jason
 */
public class CountryLostEvent extends EventObject {
    private Country defendingCountry;
    private int attackingPlayerIndex;
    public CountryLostEvent(RiskModel riskModel, Country defendingCountry, int attackingPlayerIndex) {
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
