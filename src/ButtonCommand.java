public enum ButtonCommand {
    REINFORCE("Reinforce"), ATTACK("Attack"), ENDPHASE("EndPhase"), COUNTRY("Country"), PLACETROOPS("PlaceTroops");
    private String value;

    ButtonCommand(String value) {
        this.value = value;
    }

    public String toString() {
        return value;
    }
}
