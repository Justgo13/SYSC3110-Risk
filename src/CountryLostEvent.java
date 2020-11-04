public class CountryLostEvent {
    private Country defendingCountry;
    private int attackingPlayerIndex;
    public CountryLostEvent(Country defendingCountry, int attackingPlayerIndex) {
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
