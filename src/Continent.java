import java.util.Collection;
import java.util.HashMap;

/**
 * Creates a continent object for holding countries and assigning bonus army to players
 * @author Shashaank
 */
public class Continent {
    private String name;
    private int bonusArmy;
    private HashMap<String,Country> countries;

    public Continent(String name, int bonusArmy) {
        this.name = name;
        this.bonusArmy = bonusArmy;
        countries = new HashMap<>();
    }

    public void addCountry(String name, Country country) {
        countries.put(name, country);
    }

    public String getName() {
        return name;
    }

    public int getBonusArmy() {
        return bonusArmy;
    }

    public Collection<Country> getCountries() {
        return countries.values();
    }

    public Country getCountry(String name){
        return countries.get(name);
    }


}
