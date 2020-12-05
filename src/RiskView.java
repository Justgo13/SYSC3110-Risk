import java.io.File;

public interface RiskView {
    void handleAttackEvent(AttackEvent ae);
    void handleResultEvent(BattleResultEvent bre);
    void handleEndTurn(int playerId);
    void handleDiceRolls(DiceEvent de);
    void handleDefendingCountryLost(CountryLostEvent cle);
    void handleGameOver(int playerID);
    void handleShowDefendingCountry(Country country);
    void handleCountryAttack(Country country);
    void handleShowAttackingCountry();
    void handleShowReinforceCountry();
    void handleShowReinforceAdjacents(Country country);
    void handleReinforce(Country reinforceCountry);
    void handleReinforceResultEvent(ReinforceResultEvent reinforceResultEvent);
    void handleReinforceEvent(ReinforceEvent reinforceEvent);
    void handleEndAttack(int playerID);
    void handleShowTroopPlacementCountry();
    void handleTroopPlaced(BonusTroopEvent bte);
    void troopBonusComplete();
    void handleEndBonus(int playerID);
    void handlePlayerEliminated(int playerID);
    void handleAttackCancelled(Country country);
    void handleReinforceCancelled(Country country);
}
