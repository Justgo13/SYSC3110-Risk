public enum PlayGame {
    OK_CANCEL_OPTION("OK","CANCEL"), PLAYERS("1","2","3","4","5","6"), LABEL("Select the number of players:"),
    TITLE("Choose Players"), GAMETITLE("RISK!"),
    AILABEL("Select the number of AI: "), AIPLAYERS("0","1","2","3","4","5");
    private String text;
    private String[] options;

    PlayGame(String text) {
        this.text = text;
    }

    PlayGame(String... options) {
        this.options = options;
    }

    public String[] getArray() {
        return options;
    }

    public String toString() {
        return text;
    }
}
