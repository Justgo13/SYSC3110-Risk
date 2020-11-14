public enum AttackState {
    SHOW_PLAYER_COUNTRIES, SHOW_DEFENDING_COUNTRIES, COMMENCE_ATTACK;
    private static AttackState[] vals = values();
    public AttackState next() {
        return vals[(this.ordinal()+1) % vals.length];
    }
    public AttackState previous() {
        return vals[(this.ordinal()-1) % vals.length];
    }
}
