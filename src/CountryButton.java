import javax.swing.*;
import java.awt.*;

public class CountryButton extends JButton {
    private int troopCount;
    private int playerIndex; // Will work once initialization is complete for the GUI
    private Country country;
    private String continent;
    private static final Color[] colors = {Color.red, Color.blue, Color.pink, Color.gray, Color.orange, Color.green};




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
        this.playerIndex = country.getPlayer().getId();
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
        String text ="<html>" + country.getName() + "<br> Player " + playerIndex +  "<br> Troops: " + troopCount + "<br>" + continent + "</html>";

        super.setText(text);
        super.setForeground(Color.black);
        super.setBackground(colors[playerIndex-1]);
    }

    /**
     * Author: Shashaank
     * updating the fields so they can be used directly
     */
    private void updateFields(){
        this.troopCount = country.getArmySize();
        this.playerIndex = country.getPlayer().getId();
    }

    public String getName(){
        return country.getName();
    }
}
