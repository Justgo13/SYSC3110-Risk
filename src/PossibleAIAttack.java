/**
 * Object to hold information about a possible AI attack. Holds information such as countries involved in attack,
 * probability of attacker winning attack and the expected relative score increase of the attack.
 * This object is used to help determine the best attack for the AI to make
 */
 class PossibleAIAttack {
    private Country attackingCountry;
    private Country defendingCountry;
    private double probability;
    private int relativeScoreIncrease;

    public PossibleAIAttack(Country attackingCountry, Country defendingCountry){
        this.attackingCountry=attackingCountry;
        this.defendingCountry=defendingCountry;
        this.probability=0;
        this.relativeScoreIncrease = 0;
    }


    public void setProbability(double probability){
        this.probability=probability;
    }

    public void setRelativeScoreIncrease(int score){
        this.relativeScoreIncrease =score;
    }

    public Country getAttackingCountry() {
        return attackingCountry;
    }

    public Country getDefendingCountry() {
        return defendingCountry;
    }

    public double getProbability() {
        return probability;
    }

    public int getRelativeScoreIncrease() {
        return relativeScoreIncrease;
    }
}
