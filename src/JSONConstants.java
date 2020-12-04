public enum JSONConstants {
    JSON_COUNTRIES("Countries"), GRID_BAG("gridBag"), COUNTRY_NAME("countryName"), GRID_X("x"), GRID_Y("y"),
    FILE_CHOOSER_DESC("JSON Files"), FILE_CHOOSER_TYPE("json"), DEFAULT_FILE("template.json");
    private String JSONValue;
    JSONConstants(String JSONValue) {
        this.JSONValue = JSONValue;
    }
    public String toString() {
        return JSONValue;
    }
}
