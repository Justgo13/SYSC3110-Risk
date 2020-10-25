import java.util.ArrayList;

public class Continent {
    private String name;
    private int bonusArmy;
    private ArrayList<Country> countries;

    public Continent(String name, int bonusArmy) {
        this.name = name;
        this.bonusArmy = bonusArmy;
        countries = new ArrayList<>();
    }
    /**
    Author: Shashaank
    */
    public void addCountry(Country country) {
        countries.add(country);
    }

    /**
    Author: Shashaank
    */
    public String getName() {
        return name;
    }
    /**
    Author: Shashaank
    */
    public int getBonusArmy() {
        return bonusArmy;
    }
    /**
    Author: Shashaank
    */
    public ArrayList<Country> getCountries() {
        return countries;
    }
}
