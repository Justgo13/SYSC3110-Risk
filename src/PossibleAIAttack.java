public class PossibleAIAttack {
    private Country attackingCountry;
    private Country defendingCountry;
    private double probability;
    private int relativeScoreIncrease;

    public PossibleAIAttack(double probability, Country attackingCountry, Country defendingCountry){
        this.probability = probability;
        this.attackingCountry = attackingCountry;
        this.defendingCountry= defendingCountry;
        this.relativeScoreIncrease = 0;
    }

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
