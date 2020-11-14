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
}
