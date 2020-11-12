public enum ButtonText {
    REINFORCE("REINFORCE"), ATTACK("ATTACK"), ENDTURN("END TURN");
    private String value;

    ButtonText(String value) {
        this.value = value;
    }

    public String toString() {
        return value;
    }
}
