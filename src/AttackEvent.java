import java.util.EventObject;

public class AttackEvent extends EventObject {

    private Country attackingCountry;
    private Country defendingCountry;

    /**
     * Attack Event to update the view after an attack has taken place
     *
     * @param riskModel
     * @param attackingCountry
     * @param defendingCountry
     */

    public AttackEvent(RiskGame riskModel, Country attackingCountry, Country defendingCountry) {
        super(riskModel);
        this.attackingCountry = attackingCountry;
        this.defendingCountry = defendingCountry;

    }

    public Country getAttackingCountry() {
        return attackingCountry;
    }

    public Country getDefendingCountry() {
        return defendingCountry;
    }
}
