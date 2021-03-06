import java.io.Serializable;
import java.util.ArrayList;
import java.util.Iterator;
public abstract class Player implements Serializable {
    private static final long serialVersionUID = 1L;
    private int initArmySize;
    private ArrayList<Country> countriesOwned;
    private int id;

    public Player(int initArmySize, int id) {
        this.id = id;
        this.initArmySize = initArmySize;
        countriesOwned = new ArrayList<>();
    }

    public void addCountry(Country country) {
        countriesOwned.add(country);
    }

    public void removeCountry(Country country) {
        countriesOwned.remove(country);
    }

    public int getInitArmySize() {
        return initArmySize;
    }

    public ArrayList<Country> getCountriesOwned() {
        return countriesOwned;
    }

    public ArrayList<Country> copyOfCountriesOwned() {
        ArrayList<Country> countriesValue = new ArrayList<>();
        // pass countries by value by recreating country objects
        Iterator iterator = countriesOwned.iterator();
        while (iterator.hasNext()) {
            Country country = (Country) iterator.next();
            Country newCountry = new Country(country.getName());
            newCountry.setContinent(country.getContinent());
            newCountry.setAdjacentCountries(country.getAdjacentCountries());
            newCountry.setPlayer(country.getPlayer());
            newCountry.setArmySize(country.getArmySize());
            countriesValue.add(newCountry);
        }
        return countriesValue;
    }

    public int getId() {
        return id;
    }

    public void setCountriesOwned(ArrayList<Country> countriesOwned) {
        this.countriesOwned = countriesOwned;
    }
}
