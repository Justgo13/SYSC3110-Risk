public enum ButtonCommand {
    REINFORCE("Reinforce"), ATTACK("Attack"), ENDTURN("EndTurn"), COUNTRY("Country");
    private String value;

    ButtonCommand(String value) {
        this.value = value;
    }

    public String toString() {
        return value;
    }
}