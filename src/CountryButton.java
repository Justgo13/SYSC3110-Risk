import javax.swing.*;
import java.awt.*;

/**
 * Creates a specialized JButton with extra information such as the corresponding country instance,
 * player holding the country, color, and troop count
 * @author Shashaank
 */
public class CountryButton extends JButton {
    private int troopCount;
    private int playerIndex; // Will work once initialization is complete for the GUI
    private Country country;
    private String continent;
    private static final Color[] colors = {Color.red, Color.blue, Color.pink, Color.gray, Color.orange, Color.green};

    /**
     * Instantiating fields and populating data that needs to be held
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

    public int getTroopCount() {
        return troopCount;
    }

    public Country getCountry() {
        return country;
    }

    /**
     * Will update the Country buttons text field to display the updated information
     * after events have been carried out
     * e.g. new color for new country owner, new troop count
     */
    public void update(){
        updateFields();
        String text ="<html>" + country.getName() + "<br> Player " + playerIndex +  "<br> Troops: " + troopCount + "<br>" + continent + "</html>";

        super.setText(text);
        super.setForeground(Color.black);
        super.setBackground(colors[playerIndex-1]);
}

    /**
     * Updated the troop count and player holding the current country
     */
    private void updateFields(){
        this.troopCount = country.getArmySize();
        this.playerIndex = country.getPlayer().getId();
    }

    public String getName(){
        return country.getName();
    }
}
