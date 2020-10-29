import java.util.ArrayList;

public class Country {
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

    public Player getPlayer(){ return player;}

    public int getArmySize() {
        return armySize;
    }

    public void addAdjacentCountry(Country country) {
        adjacentCountries.add(country);
    }

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
