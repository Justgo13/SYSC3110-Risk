public enum JSONConstants {
    JSON_COUNTRIES("Countries"), GRID_BAG("gridBag"), COUNTRY_NAME("countryName"), GRID_X("x"), GRID_Y("y")
    , DEFAULT_FILE("template.json");
    private String JSONValue;
    JSONConstants(String JSONValue) {
        this.JSONValue = JSONValue;
    }
    public String toString() {
        return JSONValue;
    }
}
