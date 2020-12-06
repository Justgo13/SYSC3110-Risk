public enum FileChooser {
    JSON_DESCRIPTION("JSON Files"), JSON_TYPE("json"),
    TXT_DESCRIPTION("TXT Files"), TXT_TYPE("txt");

    private String fileType;
    FileChooser(String JSONValue) {
        this.fileType = JSONValue;
    }
    public String toString() {
        return fileType;
    }
}
