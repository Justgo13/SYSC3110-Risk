import java.util.ArrayList;

public class Player {
    private int armiesToPlace;
    private int initArmySize;
    private ArrayList<Country> countriesOwned;
    private int id;

    public Player(int initArmySize, int id) {
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

    public int getId() {
        return id;
    }

    public void setCountriesOwned(ArrayList<Country> countriesOwned) {
        this.countriesOwned = countriesOwned;
    }

    public void setInitArmySize(int initArmySize) {
        this.initArmySize = initArmySize;
    }

    public void setId(int id) {
        this.id = id;
    }
}
