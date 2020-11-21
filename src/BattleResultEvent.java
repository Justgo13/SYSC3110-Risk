import java.util.EventObject;

/**
 * An event containing information about a battle result
 * @author Jason
 */
public class BattleResultEvent extends EventObject {
    private int attackingArmySize;
    private int defendingArmySize;
    private Player player;
    public BattleResultEvent(RiskModel riskModel, Player player, int attackingArmySize, int defendingArmySize) {
        super(riskModel);
        this.player = player;
        this.attackingArmySize = attackingArmySize;
        this.defendingArmySize = defendingArmySize;
    }

    public int getAttackingArmySize() {
        return attackingArmySize;
    }

    public int getDefendingArmySize() {
        return defendingArmySize;
    }

    public Player getPlayer() {return player;}
}
