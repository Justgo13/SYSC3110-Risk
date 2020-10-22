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

    public void addCountry(Country country) {
        countries.add(country);
    }
}
