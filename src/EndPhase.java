public enum EndPhase {
    BONUS_PHASE, ATTACK_PHASE, REINFORCE_PHASE, END_PHASE;
    private static EndPhase[] vals = values();
    public EndPhase next() {
        return vals[(this.ordinal()+1) % vals.length];
    }
}