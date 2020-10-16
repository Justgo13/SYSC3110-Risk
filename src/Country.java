import java.util.ArrayList;

public class Country {
    private String name;
    private Player player;
    private int armySize;
    private ArrayList<Country> adjacentCountries;

    public Country(String name) {
        this.name = name;
        armySize = 0;
        player = null;
        adjacentCountries = new ArrayList<>();
    }

    public void setArmySize(int armySize) {
        this.armySize = armySize;
    }

    public void setPlayer(Player player) {
        this.player = player;
    }

    public int getArmySize() {
        return armySize;
    }

    public void addAdjacentCountry(Country country) {
        adjacentCountries.add(country);
    }
}
