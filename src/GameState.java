public enum GameState {
    BONUS_PHASE, CHOOSE_BONUS, SHOW_PLAYER_COUNTRIES, SHOW_DEFENDING_COUNTRIES, COMMENCE_ATTACK, SHOW_REINFORCE_COUNTRIES,
    CHOOSE_REINFORCE, COMMENCE_REINFORCE;
    private static GameState[] vals = values();
    public GameState next() {
        return vals[(this.ordinal()+1) % vals.length];
    }
    public GameState prev() {return vals[(this.ordinal()-1) % vals.length];}
}
