import java.util.ArrayList;

public class Player {
    private String name;
    private int armiesToPlace;
    private int initArmySize;
    private ArrayList<Country> countriesOwned;
    private int id;

    public Player(String name, int initArmySize, int id) {
        this.name = name;
        this.id = id;
        this.armiesToPlace = 3;
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

    public String getName() {
        return name;
    }

    public int getId() {
        return id;
    }

    public void setCountriesOwned(ArrayList<Country> countriesOwned) {
        this.countriesOwned = countriesOwned;
    }
}
