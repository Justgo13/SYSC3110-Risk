import javax.swing.*;

public class CountryButton extends JButton {
    private int troopCount;

    public CountryButton(String name, int troopCount){
        super(name);
        this.troopCount=troopCount;




    }

    public void update(){
        /*
        this.setText(name + "troops: " + this.troopCount);

         */
    }
}
