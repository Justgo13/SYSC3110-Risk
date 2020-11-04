import java.util.ArrayList;

/**
 * Creates a continent object for holding countries and assigning bonus army to players
 * @author Shashaank
 */
public class Continent {
    private String name;
    private int bonusArmy;
    private ArrayList<Country> countries;

    public Continent(String name, int bonusArmy) {
        this.name = name;
        this.bonusArmy = bonusArmy;
        countries = new ArrayList<>();
    }

    public void addCountry(Country country) {
        countries.add(country);
    }

    public String getName() {
        return name;
    }

    public int getBonusArmy() {
        return bonusArmy;
    }

    public ArrayList<Country> getCountries() {
        return countries;
    }
}
