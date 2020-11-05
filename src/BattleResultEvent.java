import java.util.EventObject;

/**
 * An event containing information about a battle result
 * @author Jason
 */
public class BattleResultEvent extends EventObject {
    private int attackingArmySize;
    private int defendingArmySize;
    public BattleResultEvent(RiskModel riskModel, int attackingArmySize, int defendingArmySize) {
        super(riskModel);
        this.attackingArmySize = attackingArmySize;
        this.defendingArmySize = defendingArmySize;
    }

    public int getAttackingArmySize() {
        return attackingArmySize;
    }

    public int getDefendingArmySize() {
        return defendingArmySize;
    }
}
