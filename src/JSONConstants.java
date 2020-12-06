public enum JSONConstants {
    COUNTRIES("Countries"), GRID_BAG("gridBag"), COUNTRY_NAME("countryName"), GRID_X("x"), GRID_Y("y")
    , DEFAULT_FILE("template.json"), CONTINENTS("Continents"), ADJACENT_COUNTRY("adjacentCountries");
    private String JSONValue;
    JSONConstants(String JSONValue) {
        this.JSONValue = JSONValue;
    }
    public String toString() {
        return JSONValue;
    }
}
