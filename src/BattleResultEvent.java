public class BattleResultEvent {
    private int attackingArmySize;
    private int defendingArmySize;
    public BattleResultEvent(int attackingArmySize, int defendingArmySize) {
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
