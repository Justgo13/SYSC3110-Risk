public enum PlayGame {
    OK_CANCEL_OPTION("OK","CANCEL"), PLAYERS("1","2","3","4","5","6"), LABEL("Select the number of human players:"),
    TITLE("Choose Players"), GAME_TITLE("RISK!"),
    AI_LABEL("Select the number of AI: "), AI_PLAYERS("0","1","2","3","4","5"),
    LOAD_GAME_POPUP("Would you like to load a save?"), LOAD_MAP_POPUP("Would you like to load a custom map?"),
    INVALID_MAP_MESSAGE("This map is invalid. Please upload a valid map");
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
