public enum ReinforceState {
    SHOW_REINFORCE_COUNTRIES, COMMENCE_REINFORCE;
    private static ReinforceState[] vals = values();
    public ReinforceState next() {
        return vals[(this.ordinal()+1) % vals.length];
    }
    public ReinforceState previous() {
        return vals[(this.ordinal()-1) % vals.length];
    }
}
