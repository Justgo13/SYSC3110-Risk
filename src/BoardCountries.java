/**
 * Enum for setting up bonus armies for each country in the Board class
 * @author Jason
 */
public enum BoardCountries {
    ASIA("Asia"), NA("North America"), SA("South America"), AUS("Australia"), AFR("Africa"), EU("Europe"),
    ASIA_TROOP(7), NA_TROOP(5), SA_TROOP(2), AUS_TROOP(2), AFR_TROOP(3), EU_TROOP(5);
    private String value;
    private int troop;
    BoardCountries(String value) {
        this.value = value;
    }
    BoardCountries(int troop) {
        this.troop = troop;
    }
    public String toString() {
        return value;
    }
    public int bonusTroopCount() {
        return troop;
    }
}
