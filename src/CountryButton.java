import javax.swing.*;
import java.awt.*;
import java.util.EnumMap;

public class CountryButton extends JButton {
    private int troopCount;
    private int playerIndex; // Will work once initialization is complete for the GUI
    private Country country;
    private String continent;

    /*
    public enum Color {
        C, YELLOW, BLUE; //each is an instance of Color
    }
     */


    /**
     * Author: Shashaank Srivastava
     * instantiating fields and populating data that needs to be held
     * by the country button class
     * @param country country to get data such as continent and other info from
     */
    public CountryButton(Country country){
        super(country.getName());
        this.country = country;
        this.troopCount=country.getArmySize();
        this.continent = country.getContinent();
        //this.playerIndex = country.getPlayer().getId();
        this.update();


    }

    /**
     * Author:Shashaank
     * Will update the Countrybuttons text fields to display the updated information
     * after events have been carried out
     * e.g. new color for new country owner, new troopcount
     */
    public void update(){
        updateFields();
        String text = country.getName() +  " (" + troopCount + ", " + continent + ")"; // + country.getContinentName()
        super.setText(text);
        //super.setBackground(Color.cyan);
    }

    /**
     * Author: Shashaank
     * updating the fields so they can be used directly
     */
    private void updateFields(){
        this.troopCount = country.getArmySize();
        //this.playerIndex = country.getPlayer().getId(); //creates errors
    }
}
