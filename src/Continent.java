import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;

/**
 * Creates a continent object for holding countries and assigning bonus army to players
 * @author Shashaank
 */
public class Continent implements Serializable {
    private static final long serialVersionUID = 1L;
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

    public Country getCountry(String name){
        return countries.get(name);
    }

    public Collection<Country> getCountries(){
        return countries.values();
    }

    public ArrayList<Country> getCountriesCopy() {
        ArrayList<Country> countriesValue = new ArrayList<>();
        // pass countries by value by recreating country objects
        Iterator iterator = countries.values().iterator();
        while (iterator.hasNext()) {
            Country country = (Country) iterator.next();
            Country newCountry = new Country(country.getName());
            countriesValue.add(newCountry);
        }
        return countriesValue;
    }

    public int getBonusArmy() {
        return bonusArmy;
    }

    public Player getContinentOwner(){
        ArrayList<Country> countryArrayList = new ArrayList<>(countries.values());
        Player player = countryArrayList.get(0).getPlayer();
        for(Country c: countryArrayList){
            if(c.getPlayer() != player){
                return null;
            }
        }
        return player;
    }
}
