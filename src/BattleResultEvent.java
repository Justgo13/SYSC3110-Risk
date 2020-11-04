import java.util.EventObject;

public class BattleResultEvent extends EventObject {
    private int attackingArmySize;
    private int defendingArmySize;
    public BattleResultEvent(RiskGame riskModel, int attackingArmySize, int defendingArmySize) {
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
