import java.io.Serializable;
import java.util.ArrayList;

/**
 * Creates a country that holds information such as current player and troop count which is used as data for the model.
 * @author Jason
 */
public class Country implements Serializable {
    private static final long serialVersionUID = 1L;
    private String name;
    private Player player;
    private int armySize;
    private ArrayList<Country> adjacentCountries;
    private String continent;

    public Country(String name) {
        this.name = name;
        armySize = 0;
        player = null;
        adjacentCountries = new ArrayList<>();
    }
    public void setContinent(String continent){
        this.continent = continent;
    }

    public String getName () {return name;}
    public String getContinent() {return continent;}

    public void setArmySize(int armySize) {
        this.armySize = armySize;
    }

    public void setPlayer(Player player) {
        this.player = player;
    }

    public void removeArmy() {
        this.armySize--;
    }

    public void addArmy(){this.armySize++;}

    public Player getPlayer(){ return player;}

    public int getArmySize() {
        return armySize;
    }

    public void addAdjacentCountry(Country country) {
        adjacentCountries.add(country);
    }

    public void setAdjacentCountries(ArrayList<Country> adjacentCountries) {this.adjacentCountries = adjacentCountries; }

    public ArrayList<Country> getAdjacentCountries(){return adjacentCountries;}


    /** Returns the adjacent countries that can be attacked
     *
     * @return Arraylist of Country objects that are adjacent and have a different owner
     * @author Harjap Gill
     */

    public ArrayList<Country> getPossibleAttacks(){
        ArrayList<Country> countries = new ArrayList<Country>();

        for (Country country:adjacentCountries){
            if (!(country.getPlayer().equals(this.player))){
                countries.add(country);
            }
        }
        return countries;
    }

}
